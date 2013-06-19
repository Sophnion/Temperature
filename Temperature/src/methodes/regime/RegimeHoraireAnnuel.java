package methodes.regime;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
 * The Class RegimeHoraireAnnuel.
 */
public class RegimeHoraireAnnuel {

	private static TimeSeriesCollection dataset;

	private static JFreeChart chart;

	/**
	 * Creates the data set.
	 * 
	 * @param id
	 *            the id
	 */
	private static void createDataSet(int id) {
		dataset = new TimeSeriesCollection();
		Station station = MapController.getStationList().getStationById(id);
		RecordMois[] recordList = new RecordMois[12];
		for (int i = 0; i < 12; i++) {
			recordList[i] = new RecordMois();
		}
		if (station != null) {
			ArrayList<Chronique> chroniqueList = station.getChroniqueList();
			if (chroniqueList.isEmpty() == false) {
				for (Chronique ch : chroniqueList) {
					int mois = ch.getMois() - 1;
					int heure = ch.getHeure();
					recordList[mois].ajoutRecord(heure, ch.getTemperature());
				}
				for (int i = 0; i < 12; i++) {
					String name = null;
					try {
						Date month = new SimpleDateFormat("M").parse(String
								.valueOf(i + 1));
						name = new SimpleDateFormat("MMMM").format(month);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					TimeSeries data = new TimeSeries(name);
					for (int j = 0; j < 24; j++) {
						double valeur = recordList[i].getMoyenHeure(j)
								/ recordList[i].getMoyenMois();
						data.add(new Hour(j, 1, 1, 1900), valeur);
					}
					dataset.addSeries(data);
				}
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
				"Echelle Normalisée",// ValueAxisLabel
				dataset, // dataset
				true, // legend
				true, // tooltips
				false);// urls
		XYPlot xyplot = (XYPlot) chart.getPlot();
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);
		NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		numberaxis.setAutoRange(false);
		dateaxis.setAutoRange(false);
		numberaxis.setRange(DatasetUtilities.findRangeBounds(dataset));
		dateaxis.setRange(DatasetUtilities.findDomainBounds(dataset));
		dateaxis.setDateFormatOverride(new SimpleDateFormat("HH"));
		XYLineAndShapeRenderer render = new XYLineAndShapeRenderer();
		render.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
				"{0}:{1}h {2}", new SimpleDateFormat("HH"), new DecimalFormat(
						"0.0000")));
		xyplot.setRenderer(render);
		return chart;
	}

}
