package methodes.chronique;

import java.util.ArrayList;

import model.Chronique;
import model.Station;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import controller.MapController;

/**
 * The Class ChroniqueHoraire.
 */
public class ChroniqueHoraire {

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
		TimeSeries data = new TimeSeries("Température(°C)");
		Station station = MapController.getStationList().getStationById(id);
		if (station != null) {
			ArrayList<Chronique> chroniqueList = station.getChroniqueList();
			if (chroniqueList.isEmpty() == false) {
				for (Chronique ch : chroniqueList) {
					data.addOrUpdate(
							new Hour(ch.getHeure(), ch.getJour(), ch.getMois(),
									ch.getAnnee()), ch.getTemperature());
				}
			}
		}
		dataset.addSeries(data);
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
				"Températures(°C)",// ValueAxisLabel
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
		return chart;
	}
}
