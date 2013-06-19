package methodes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import model.Chronique;
import model.Station;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import controller.MapController;

/**
 * The Class DeltaJournalier.
 */
public class DeltaJournalier {

	private static TimeSeriesCollection dataset;

	private static JFreeChart chart;

	private static int nb_jour = 3;

	private static double coeff = 1;

	private static int nb_err;

	/**
	 * Creates the data set.
	 * 
	 * @param id1
	 *            the id1
	 * @param id2
	 *            the id2
	 */
	private static void createDataSet(int id1, int id2) {
		dataset = new TimeSeriesCollection();
		nb_err = 0;

		TimeSeries data1 = new TimeSeries("Station " + id1);
		TimeSeries data2 = new TimeSeries("Station " + id2);
		TimeSeries data_err = new TimeSeries("Points Faux");
		TimeSeries data_ecart_inf = new TimeSeries(
				"Ecart Type : borne Inférieure");
		TimeSeries data_ecart_sup = new TimeSeries(
				"Ecart Type : borne Supérieure");
		TimeSeries data_diff = new TimeSeries("Diff");

		// charger data chronique journaliere pour station 1
		Station station1 = MapController.getStationList().getStationById(id1);
		if (station1 != null) {
			ArrayList<Chronique> chroniqueList = station1.getChroniqueList();
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
							data1.addOrUpdate(new Day(jour, mois, annee), somme
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

		// charger data chronique journaliere pour satation2
		Station station2 = MapController.getStationList().getStationById(id2);
		if (station2 != null) {
			ArrayList<Chronique> chroniqueList2 = station2.getChroniqueList();
			if (chroniqueList2.isEmpty() == false) {
				boolean firsttime = true;
				int jour = 0, mois = 0, annee = 0;
				double somme = 0, nb = 0;
				for (Chronique ch : chroniqueList2) {
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
							data2.addOrUpdate(new Day(jour, mois, annee), somme
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

		// ajout des données dans data_diff
		@SuppressWarnings("rawtypes")
		Iterator iterator = data1.getTimePeriods().iterator();
		while (iterator.hasNext()) {
			RegularTimePeriod period = (RegularTimePeriod) iterator.next();
			if (data2.getDataItem(period) != null) {
				data_diff.addOrUpdate(period, (Double) data2
						.getDataItem(period).getValue()
						- (Double) data1.getDataItem(period).getValue());
			} else {
				data_diff.addOrUpdate(period, 0);
			}
		}

		dataset.addSeries(data_err);
		dataset.addSeries(data1);
		dataset.addSeries(data2);
		dataset.addSeries(data_ecart_sup);
		dataset.addSeries(data_ecart_inf);
		dataset.addSeries(data_diff);
	}

	/**
	 * Creates the chart.
	 * 
	 * @param idStation1
	 *            the id station1
	 * @param idStation2
	 *            the id station2
	 * @return the j free chart
	 */
	public static JFreeChart createChart(int idStation1, int idStation2) {
		createDataSet(idStation1, idStation2);
		chart = ChartFactory.createTimeSeriesChart(idStation1 + " : "
				+ MapController.getStationList().getNameById(idStation1) + "\n"
				+ idStation2 + " : "
				+ MapController.getStationList().getNameById(idStation2), // titre
				"Heure", // TimeAxisLabel
				"températures(°C)",// ValueAxisLabel
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

		XYItemRenderer render = xyplot.getRenderer();
		render.setSeriesPaint(2, Color.GREEN);
		render.setSeriesPaint(3, Color.BLACK);
		render.setSeriesPaint(4, Color.BLACK);
		render.setSeriesPaint(5, Color.YELLOW);
		xyplot.setRenderer(render);
		return chart;
	}

	/**
	 * Ecart type.
	 * 
	 * @param recordList
	 *            the record list
	 * @return the double
	 */
	private static double ecartType(ArrayList<Double> recordList) {
		double somme = 0, moy = 0;
		for (Double item : recordList) {
			somme += item;
		}
		moy = somme / recordList.size();
		somme = 0;
		for (Double item : recordList) {
			somme += (item - moy) * (item - moy);
		}
		return Math.sqrt(somme / recordList.size());
	}
}
