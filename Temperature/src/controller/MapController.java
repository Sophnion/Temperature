package controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import model.Map;
import model.Station;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.swing.JMapPane;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.table.FeatureCollectionTableModel;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.Geometry;

import vue.MultiStationWindow;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * The Class MapController.
 */
public class MapController {

	private MapContent map;

	private static Map stationList;

	private static String id;

	private int ostralher;

	private JFileChooser chooseFile;

	private File file;

	private FileFilter filterFile;

	private FileDataStore store;

	private FeatureSource<?, ?> featureSource;

	private FeatureCollection<?, ?> selectedFeatures;

	private FeatureCollection<?, ?> selectedProcheFeatures;

	private StyleFactory styleFactory = CommonFactoryFinder
			.getStyleFactory(null);

	private FilterFactory2 filterFactory = CommonFactoryFinder
			.getFilterFactory2(null);

	private static final Color SELECTED_COLOR = Color.GREEN;

	private static final Color MID2_COLOR = Color.MAGENTA;

	private static final Color MIDDLE_COLOR = Color.ORANGE;

	private static final Color HIGH_COLOR = Color.RED;

	private static final Color LINE_COLOR = Color.BLUE;

	private static final Color FILL_COLOR = Color.CYAN;

	private static final float OPACITY = 1.0f;

	private static final float LINE_WIDTH = 1.0f;

	private static final float POINT_SIZE = 6.0f;

	/**
	 * The Enum GeomType.
	 */
	private enum GeomType {

		POINT,

		LINE,

		POLYGON
	};

	private String geometryAttributeName;

	private GeomType geometryType;

	private Layer layer;

	/**
	 * Gets the station list.
	 * 
	 * @return the station list
	 */
	public static Map getStationList() {
		return stationList;
	}

	/**
	 * Instantiates a new map controller.
	 */
	public MapController() {
		ostralher = 0;
		map = new MapContent();
		stationList = new Map();
		chooseFile = new JFileChooser();

		/*
		 * créer le filter pour choisir les fichiers de type .shp
		 */
		filterFile = new FileFilter() {
			@Override
			public String getDescription() {
				return "Shape Files *.shp";
			}

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				String ext = null;
				String name = f.getName();
				int i = name.lastIndexOf('.');
				if (i > 0 && i < name.length() - 1) {
					ext = name.substring(i + 1).toLowerCase();
				}
				if (ext != null && ext.equals("shp"))
					return true;
				return false;
			}
		};
	}

	/**
	 * Gets the map.
	 * 
	 * @return the map
	 */
	public MapContent getMap() {
		return map;
	}

	/**
	 * Choose file.
	 * 
	 * @return true, if successful
	 */
	public boolean chooseFile() {

		// pour mieux debugger
		chooseFile.setDialogTitle("Ajout du fichier .shp");
		chooseFile.setFileFilter(filterFile);
		int result = chooseFile.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			file = chooseFile.getSelectedFile();
			return true;
		}
		return false;

		// file = new File(
		// "/home/yuan/Documents/Stage_Temperature/FINI/shapefile/stations_RNT_ordre_pk.shp");
		// return true;
	}

	/**
	 * Charge shape file.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void chargeShapeFile() throws Exception {
		store = FileDataStoreFinder.getDataStore(file);
		featureSource = store.getFeatureSource();

		/*
		 * create station list
		 */
		SimpleFeatureCollection allFeatures = (SimpleFeatureCollection) featureSource
				.getFeatures();
		SimpleFeatureIterator iter = allFeatures.features();
		try {
			while (iter.hasNext()) {
				SimpleFeature feature = iter.next();
				Station st = new Station();
				st.setIdStation((int) feature.getAttribute("id_station"));
				st.setNameStation(feature.getAttribute("localisati").toString());
				stationList.ajoutStation(st);
			}
		} finally {
			iter.close();
		}

		setGeometry();
		layer = new FeatureLayer(featureSource, createDefaultStyle());
		map.addLayer(layer);
	}

	/**
	 * Sets the geometry.
	 */
	private void setGeometry() {
		GeometryDescriptor geomDesc = featureSource.getSchema()
				.getGeometryDescriptor();
		geometryAttributeName = geomDesc.getLocalName();

		Class<?> clazz = geomDesc.getType().getBinding();

		if (Polygon.class.isAssignableFrom(clazz)
				|| MultiPolygon.class.isAssignableFrom(clazz)) {
			geometryType = GeomType.POLYGON;

		} else if (LineString.class.isAssignableFrom(clazz)
				|| MultiLineString.class.isAssignableFrom(clazz)) {

			geometryType = GeomType.LINE;

		} else {
			geometryType = GeomType.POINT;
		}

	}

	/**
	 * Creates the default style.
	 * 
	 * @return the style
	 * @throws Exception
	 *             the exception
	 */
	private Style createDefaultStyle() throws Exception {
		Rule rule1 = createRule(LINE_COLOR, FILL_COLOR, POINT_SIZE);
		rule1.setFilter(CQL.toFilter("nbmes='0'"));

		Rule rule2 = createRule(LINE_COLOR, MIDDLE_COLOR, POINT_SIZE);
		rule2.setFilter(filterFactory.and(CQL.toFilter("nbmes>'0'"),
				CQL.toFilter("nbmes<'15000'")));

		Rule rule3 = createRule(LINE_COLOR, MID2_COLOR, POINT_SIZE);
		rule3.setFilter(filterFactory.and(CQL.toFilter("nbmes>'15001'"),
				CQL.toFilter("nbmes<'20000'")));

		Rule rule4 = createRule(LINE_COLOR, HIGH_COLOR, POINT_SIZE);
		rule4.setFilter(CQL.toFilter("nbmes>'20001'"));

		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();
		fts.rules().add(rule1);
		fts.rules().add(rule2);
		fts.rules().add(rule3);
		fts.rules().add(rule4);

		Style style = styleFactory.createStyle();
		style.featureTypeStyles().add(fts);
		return style;
	}

	/**
	 * Creates the select style.
	 * 
	 * @param iDs
	 *            the i ds
	 * @return the style
	 * @throws Exception
	 *             the exception
	 */
	private Style createSelectStyle(Set<FeatureId> iDs) throws Exception {
		Rule rule1 = createRule(LINE_COLOR, FILL_COLOR, POINT_SIZE);
		rule1.setFilter(CQL.toFilter("nbmes='0'"));

		Rule rule2 = createRule(LINE_COLOR, MIDDLE_COLOR, POINT_SIZE);
		rule2.setFilter(filterFactory.and(CQL.toFilter("nbmes>'0'"),
				CQL.toFilter("nbmes<'15000'")));

		Rule rule3 = createRule(LINE_COLOR, MID2_COLOR, POINT_SIZE);
		rule3.setFilter(filterFactory.and(CQL.toFilter("nbmes>'15001'"),
				CQL.toFilter("nbmes<'20000'")));

		Rule rule4 = createRule(LINE_COLOR, HIGH_COLOR, POINT_SIZE);
		rule4.setFilter(CQL.toFilter("nbmes>'20001'"));

		Rule selectedRule = createRule(SELECTED_COLOR, SELECTED_COLOR,
				POINT_SIZE + 5);
		selectedRule.setFilter(filterFactory.id(iDs));

		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();
		fts.rules().add(rule1);
		fts.rules().add(rule2);
		fts.rules().add(rule3);
		fts.rules().add(rule4);
		fts.rules().add(selectedRule);

		Style style = styleFactory.createStyle();
		style.featureTypeStyles().add(fts);
		return style;
	}

	/**
	 * Creates the rule.
	 * 
	 * @param outlineColor
	 *            the outline color
	 * @param fillColor
	 *            the fill color
	 * @param pointSize
	 *            the size
	 * @return the rule
	 */
	private Rule createRule(Color outlineColor, Color fillColor, float pointSize) {
		Symbolizer symbolizer = null;
		Fill fill = null;
		Stroke stroke = styleFactory.createStroke(
				filterFactory.literal(outlineColor),
				filterFactory.literal(LINE_WIDTH));

		switch (geometryType) {
		case POLYGON:
			fill = styleFactory.createFill(filterFactory.literal(fillColor),
					filterFactory.literal(OPACITY));
			symbolizer = styleFactory.createPolygonSymbolizer(stroke, fill,
					geometryAttributeName);
			break;

		case LINE:
			symbolizer = styleFactory.createLineSymbolizer(stroke,
					geometryAttributeName);
			break;

		case POINT:
			fill = styleFactory.createFill(filterFactory.literal(fillColor),
					filterFactory.literal(OPACITY));

			Mark mark = styleFactory.getCircleMark();
			mark.setFill(fill);
			mark.setStroke(stroke);

			Graphic graphic = styleFactory.createDefaultGraphic();
			graphic.graphicalSymbols().clear();
			graphic.graphicalSymbols().add(mark);
			graphic.setSize(filterFactory.literal(pointSize));

			symbolizer = styleFactory.createPointSymbolizer(graphic,
					geometryAttributeName);
		}

		Rule rule = styleFactory.createRule();
		rule.symbolizers().add(symbolizer);
		return rule;
	}

	/**
	 * Select features.
	 * 
	 * @param ev
	 *            the ev
	 * @param mf
	 *            the mf
	 * @return the string
	 * @throws CQLException
	 *             the cQL exception
	 */
	public String selectFeatures(MapMouseEvent ev, JMapPane mf)
			throws CQLException {
		String result = "";
		System.out.println("Mouse click at: " + ev.getPoint());

		/*
		 * Construct a 5x5 pixel rectangle centered on the mouse click position
		 */
		Point screenPos = ev.getPoint();
		Rectangle screenRect = new Rectangle(screenPos.x - 2, screenPos.y - 2,
				5, 5);
		DirectPosition2D directPos = ev.getWorldPos();
		Rectangle procheRect = new Rectangle((int) directPos.x - 25000,
				(int) directPos.y - 25000, 50000, 50000);

		/*
		 * Transform the screen rectangle into bounding box in the coordinate
		 * reference system of our map context. Note: we are using a naive
		 * method here but GeoTools also offers other, more accurate methods.
		 */
		AffineTransform screenToWorld = mf.getScreenToWorldTransform();
		Rectangle2D worldRect = screenToWorld
				.createTransformedShape(screenRect).getBounds2D();
		ReferencedEnvelope bbox = new ReferencedEnvelope(worldRect, mf
				.getMapContent().getCoordinateReferenceSystem());
		ReferencedEnvelope bboxProche = new ReferencedEnvelope(procheRect, mf
				.getMapContent().getCoordinateReferenceSystem());

		/*
		 * Create a Filter to select features that intersect with the bounding
		 * box
		 */
		Filter filter = filterFactory.intersects(
				filterFactory.property(featureSource.getSchema()
						.getGeometryDescriptor().getLocalName()),
				filterFactory.literal(bbox));

		Filter filterProche = filterFactory.and(filterFactory.bbox(
				filterFactory.property(geometryAttributeName), bboxProche), CQL
				.toFilter("nbmes>'0'"));

		/*
		 * Use the filter to identify the selected features
		 */
		try {
			selectedFeatures = (SimpleFeatureCollection) featureSource
					.getFeatures(filter);

			SimpleFeatureIterator iter = (SimpleFeatureIterator) selectedFeatures
					.features();
			Set<FeatureId> IDs = new HashSet<FeatureId>();
			try {
				while (iter.hasNext()) {
					SimpleFeature feature = iter.next();
					ostralher = (int) feature.getAttribute("OSTRAHLER");
					IDs.add(feature.getIdentifier());
					result += displayInformation(feature);
					System.out.println("   "
							+ feature.getAttribute("id_station"));
					id = String.valueOf(feature.getAttribute("id_station"));
				}

			} finally {
				iter.close();
			}

			if (IDs.isEmpty()) {
				System.out.println("   no feature selected");
			}

			Style style = createSelectStyle(IDs);
			map.removeLayer(layer);
			layer = new FeatureLayer(featureSource, style);
			map.addLayer(layer);
			mf.repaint();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		/*
		 * les stations proches
		 */
		try {
			selectedProcheFeatures = (SimpleFeatureCollection) featureSource
					.getFeatures(filterProche);

			SimpleFeatureIterator iter = (SimpleFeatureIterator) selectedProcheFeatures
					.features();
			Set<FeatureId> IDProches = new HashSet<FeatureId>();
			try {
				while (iter.hasNext()) {
					SimpleFeature feature = iter.next();
					if ((int) feature.getAttribute("OSTRAHLER") == ostralher) {
						IDProches.add(feature.getIdentifier());
					}
				}

			} finally {
				iter.close();
			}

			if (IDProches.isEmpty()) {
				System.out.println("   no proche feature selected");
			} else {
				selectedProcheFeatures = featureSource
						.getFeatures(filterFactory.id(IDProches));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return result;
	}

	/**
	 * Display information.
	 * 
	 * @param feature
	 *            the feature
	 * @return the string
	 */
	private String displayInformation(Feature feature) {
		String area = "\n";
		Collection<Property> props = feature.getProperties();
		String valueStr = null;
		for (Property prop : props) {
			String name = prop.getName().getLocalPart();
			Object value = prop.getValue();

			if (value instanceof Geometry) {
				name = "  Geometry";
				valueStr = value.getClass().getSimpleName();
			} else {
				valueStr = value.toString();
			}
			if (name.equalsIgnoreCase("ombrage")) {
				area += "Ombrage" + ": " + valueStr + "\n";
			}
			if (name.equalsIgnoreCase("type_proj_")) {
				area += "Type Projection" + ": " + valueStr + "\n";
			}
			if (name.equalsIgnoreCase("Site_hydro")) {
				area += "site_hydro" + ": " + valueStr + "\n";
			}
			if (name.equalsIgnoreCase("Code_wama")) {
				area += "code_wama" + ": " + valueStr + "\n";
			}
			if (name.equalsIgnoreCase("Libelle")) {
				area += "libelle" + ": " + valueStr + "\n";
			}
			if (name.equalsIgnoreCase("Nbmes")) {
				area += "Nombre mesures" + ": " + valueStr + "\n";
			}
			if (name.equalsIgnoreCase("Statut")) {
				area += "statut" + ": " + valueStr + "\n";
			}
			if (name.equalsIgnoreCase("OSTRAHLER")) {
				area += "Ordre de Strahler" + ": " + valueStr + "\n";
				ostralher = Integer.valueOf(valueStr);
			}
			if (name.equalsIgnoreCase("pkAval")) {
				area += "pkAval(km)" + ": "
						+ Math.round(Float.parseFloat(valueStr)) + "\n";
			}
			if (name.equalsIgnoreCase("pkAmont")) {
				area += "pkAmont(km)" + ": "
						+ Math.round(Float.parseFloat(valueStr)) + "\n";
			}
			if (name.equalsIgnoreCase("id_station")) {
				area += "Identifiant Station" + ": " + valueStr + "\n";
				id = valueStr;
			}
			if (name.equalsIgnoreCase("code_depar")) {
				area += "dept" + ": " + valueStr + "\n";
			}
			if (name.equalsIgnoreCase("localisati")) {
				area += "localisation" + ": " + valueStr + "\n";
			}
			if (name.equalsIgnoreCase("x_agence")) {
				area += "xL93" + ": " + valueStr + "\n";
			}
			if (name.equalsIgnoreCase("y_agence")) {
				area += "yL93" + ": " + valueStr + "\n";
			}
			if (name.equalsIgnoreCase("type_proj")) {
				area += "projection" + ": " + valueStr + "\n";
			}
		}
		area.concat("\n");
		return area;
	}

	/**
	 * Gets the select feature id.
	 * 
	 * @return the select feature id
	 */
	public String getSelectFeatureID() {
		return id;
	}

	/**
	 * Display information.
	 * 
	 * @param id
	 *            the id
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String displayInformation(int id, JMapPane mf) throws IOException {
		String area = "";
		Set<FeatureId> ID = new HashSet<FeatureId>();
		SimpleFeatureCollection features = (SimpleFeatureCollection) featureSource.getFeatures();
		SimpleFeatureIterator iter = features.features();
		try {
			while (iter.hasNext()) {
				SimpleFeature feature = iter.next();
				if ((int) feature.getAttribute("id_station") == id) {
					area = displayInformation(feature);
					ID.add(feature.getIdentifier());
					break;
				}
			}
		} finally {
			iter.close();
		}
		
		if(!ID.isEmpty()){
			try {
				Style style = createSelectStyle(ID);
				map.removeLayer(layer);
				layer = new FeatureLayer(featureSource, style);
				map.addLayer(layer);
				mf.repaint();
			} catch (Exception e) {
				// si on a pas reussi à trouver la station
				e.printStackTrace();
			}
			
		}
		return area;
	}

	/**
	 * Creates the multi station window.
	 */
	public void createMultiStationWindow() {
		FeatureCollectionTableModel model = new FeatureCollectionTableModel(
				(SimpleFeatureCollection) selectedProcheFeatures);
		new MultiStationWindow(model, Integer.valueOf(id));
	}

}
