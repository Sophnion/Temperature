package methodes.regime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Chronique;
import model.Station;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;
import org.jfree.date.DateUtilities;

import controller.MapController;

/**
 * The Class RegimeSaisonnier2.
 */
public class RegimeSaisonnier2 {

	private static DefaultBoxAndWhiskerXYDataset dataset;

	private static JFreeChart chart;

	/**
	 * Creates the data set.
	 * 
	 * @param id
	 *            the id
	 */
	private static void createDataSet(int id) {
		dataset = new DefaultBoxAndWhiskerXYDataset("Régime Saisonnier 2");
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
					Date date = DateUtilities.createDate(1900, i + 1, 1, 0, 0);
					dataset.add(date, BoxAndWhiskerCalculator
							.calculateBoxAndWhiskerStatistics(recordList[i]
									.getRecord()));
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
		chart = ChartFactory.createBoxAndWhiskerChart(idStation + " : "
				+ MapController.getStationList().getNameById(idStation), // titre
				"Mois",// ValueAxisLabel
				null, dataset, false);
		XYPlot xyplot = (XYPlot) chart.getPlot();
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);
		NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		numberaxis.setAutoRange(false);
		numberaxis.setRange(DatasetUtilities.findRangeBounds(dataset));
		dateaxis.setDateFormatOverride(new SimpleDateFormat("MMM"));

		return chart;
	}
}
