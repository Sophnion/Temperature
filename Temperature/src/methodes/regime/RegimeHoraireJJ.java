package methodes.regime;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.Chronique;
import model.Station;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import controller.MapController;

/**
 * The Class RegimeHoraireJJ.
 */
public class RegimeHoraireJJ {

	private static TimeSeriesCollection datasetJanv;

	private static TimeSeriesCollection datasetJuillet;

	private static JFreeChart chart;

	/**
	 * Creates the data set.
	 * 
	 * @param id
	 *            the id
	 */
	private static void createDataSet(int id) {
		datasetJanv = new TimeSeriesCollection();
		datasetJuillet = new TimeSeriesCollection();
		TimeSeries dataJuillet = new TimeSeries("Régime Horaire Juillet");
		TimeSeries dataJanv = new TimeSeries("Régime Horaire Janvier");
		RecordMois[] recordList = new RecordMois[2];
		for (int i = 0; i < 2; i++) {
			recordList[i] = new RecordMois();
		}
		Station station = MapController.getStationList().getStationById(id);
		if (station != null) {
			ArrayList<Chronique> chroniqueList = station.getChroniqueList();
			if (chroniqueList.isEmpty() == false) {
				for (Chronique ch : chroniqueList) {
					if (ch.getMois() == 1) { // Janvier
						recordList[0].ajoutRecord(ch.getHeure(),
								ch.getTemperature());
					} else if (ch.getMois() == 7) { // Juillet
						recordList[1].ajoutRecord(ch.getHeure(),
								ch.getTemperature());
					}
				}
				for (int j = 0; j < 24; j++) {
					double valeur0 = recordList[0].getMoyenHeure(j);
					dataJanv.add(new Hour(j, 1, 1, 1900), valeur0);
					double valeur1 = recordList[1].getMoyenHeure(j);
					dataJuillet.add(new Hour(j, 1, 1, 1900), valeur1);
				}
				datasetJuillet.addSeries(dataJuillet);
				datasetJanv.addSeries(dataJanv);
			}
		}
	}

	/**
	 * Creates the chart.
	 * 
	 * @param idStation
	 *            the id station
	 * @return the j free chart
	 */
	public static JFreeChart createChart(int idStation) {
		createDataSet(idStation);
		chart = ChartFactory.createTimeSeriesChart(idStation + " : "
				+ MapController.getStationList().getNameById(idStation), // titre
				"Heure", // TimeAxisLabel
				"Températures mois chaud(°C)",// ValueAxisLabel
				datasetJuillet, // dataset
				true, // legend
				true, // tooltips
				false);// urls
		XYPlot xyplot = (XYPlot) chart.getPlot();
		NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		numberaxis.setAutoRange(false);
		dateaxis.setAutoRange(false);
		numberaxis.setRange(DatasetUtilities.findRangeBounds(datasetJuillet));
		dateaxis.setRange(DatasetUtilities.findDomainBounds(datasetJuillet));
		dateaxis.setDateFormatOverride(new SimpleDateFormat("HH"));

		// deuxième axis y droit
		NumberAxis numberaxis2 = new NumberAxis("Températures mois froid(°C)");
		numberaxis2.setRange(DatasetUtilities.findRangeBounds(datasetJanv));
		xyplot.setRangeAxis(1, numberaxis2);
		xyplot.setDataset(1, datasetJanv);
		xyplot.mapDatasetToRangeAxis(1, 1);
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);

		StandardXYToolTipGenerator tooltip = new StandardXYToolTipGenerator(
				"{0}:{1}h {2}", new SimpleDateFormat("HH"), new DecimalFormat(
						"0.00"));

		XYLineAndShapeRenderer renderY2 = new XYLineAndShapeRenderer();
		renderY2.setSeriesPaint(0, Color.BLUE);
		renderY2.setBaseToolTipGenerator(tooltip);
		xyplot.setRenderer(1, renderY2);

		XYLineAndShapeRenderer renderXY1 = new XYLineAndShapeRenderer();
		renderXY1.setBaseToolTipGenerator(tooltip);
		xyplot.setRenderer(renderXY1);

		return chart;
	}

}
