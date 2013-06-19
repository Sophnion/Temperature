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
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import controller.MapController;

/**
 * The Class ChroniqueJournalier.
 */
public class ChroniqueJournalier {

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
		TimeSeries data = new TimeSeries("Température Moyen Journalier(°C)");
		Station station = MapController.getStationList().getStationById(id);
		if (station != null) {
			ArrayList<Chronique> chroniqueList = station.getChroniqueList();
			if (chroniqueList.isEmpty() == false) {
				boolean firsttime = true;
				int jour = 0, mois = 0, annee = 0;
				double somme = 0, nb = 0;
				for (Chronique ch : chroniqueList) {
					if (firsttime) {
						jour = ch.getJour();
						mois = ch.getMois();
						annee = ch.getAnnee();
						somme = ch.getTemperature();
						nb++;
						firsttime = false;
					} else {
						if (ch.getJour() == jour && ch.getMois() == mois
								&& ch.getAnnee() == annee) {
							somme += ch.getTemperature();
							nb++;
						} else {
							data.addOrUpdate(new Day(jour, mois, annee), somme
									/ nb);
							jour = ch.getJour();
							mois = ch.getMois();
							annee = ch.getAnnee();
							somme = ch.getTemperature();
							nb = 1;
						}
					}
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
				"Date", // TimeAxisLabel
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
