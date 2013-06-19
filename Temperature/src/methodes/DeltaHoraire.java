package methodes;

import java.awt.Color;
import java.util.ArrayList;

import model.Chronique;
import model.Station;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import controller.MapController;

/**
 * The Class DeltaHoraire.
 */
public class DeltaHoraire {

	private static TimeSeriesCollection dataset;

	private static JFreeChart chart;

	private static int nb_jour = 45;

	private static double coeff = 5;

	private static int nb_err;

	/**
	 * Creates the data set.
	 * 
	 * @param id
	 *            the id
	 */
	private static void createDataSet(int id) {
		dataset = new TimeSeriesCollection();
		nb_err = 0;

		/*
		 * création de timeseries
		 */
		TimeSeries data = new TimeSeries("Temperature");
		TimeSeries data_err = new TimeSeries("Points Faux");
		TimeSeries data_ecart_inf = new TimeSeries(
				"Ecart Type : borne Inférieure");
		TimeSeries data_ecart_sup = new TimeSeries(
				"Ecart Type : borne Supérieure");
		TimeSeries data_diff = new TimeSeries("Diff");

		ArrayList<RecordJour> recordList = new ArrayList<RecordJour>();

		Station station = MapController.getStationList().getStationById(id);
		if (station != null) {
			ArrayList<Chronique> chroniqueList = station.getChroniqueList();
			if (chroniqueList.isEmpty() == false) {
				int heure = 0, jour = 0, cpt_jour = 0;
				double temperature = 0, ecart = 0;
				boolean firsttime = true;
				RecordJour record = null;
				for (Chronique ch : chroniqueList) {
					if (firsttime == true) {
						heure = ch.getHeure();
						jour = ch.getJour();
						temperature = ch.getTemperature();
						firsttime = false;
						ecart = 0;
						record = new RecordJour();
						data_diff.addOrUpdate(
								new Hour(ch.getHeure(), ch.getJour(), ch
										.getMois(), ch.getAnnee()),
										ch.getTemperature() - temperature);
					}
					// ajout des données pour le courbe de température
					data.addOrUpdate(
							new Hour(ch.getHeure(), ch.getJour(), ch.getMois(),
									ch.getAnnee()), ch.getTemperature());
					record.ajoutRecord(ch.getTemperature() - temperature);
					// ajout des données pour le courbe de différence
					if (heure + 1 == ch.getHeure()
							|| heure - ch.getHeure() == 23) {
						data_diff.addOrUpdate(
								new Hour(ch.getHeure(), ch.getJour(), ch
										.getMois(), ch.getAnnee()),
										ch.getTemperature() - temperature);
					}
					// ajout des données pour les courbes d'écart type
					if (jour != ch.getJour()) {
						jour = ch.getJour();
						cpt_jour++;
						recordList.add(record);
						record = new RecordJour();
						if (cpt_jour == nb_jour) {
							ecart = ecartType(recordList);
							recordList.remove(0);
							cpt_jour--;
						}
					}
					data_ecart_inf.addOrUpdate(
							new Hour(ch.getHeure(), ch.getJour(), ch.getMois(),
									ch.getAnnee()), -ecart * coeff);
					data_ecart_sup.addOrUpdate(
							new Hour(ch.getHeure(), ch.getJour(), ch.getMois(),
									ch.getAnnee()), ecart * coeff);

					// mise à jour les valeurs temporaires
					heure = ch.getHeure();
					temperature = ch.getTemperature();
				}
			}
		}
		/*
		 * //ajout des points faux Iterator iterator =
		 * data.getTimePeriods().iterator(); while(iterator.hasNext()){
		 * RegularTimePeriod period = (RegularTimePeriod)iterator.next(); double
		 * temperature = (Double)data.getDataItem(period).getValue(); double
		 * diff = (Double)data_diff.getDataItem(period).getValue(); if(diff <
		 * (Double)data_ecart_inf.getDataItem(period).getValue() || diff >
		 * (Double)data_ecart_sup.getDataItem(period).getValue()){
		 * data_err.addOrUpdate(period, temperature); nb_err++; } }
		 */
		dataset.addSeries(data_err);
		dataset.addSeries(data);
		dataset.addSeries(data_ecart_sup);
		dataset.addSeries(data_ecart_inf);
		dataset.addSeries(data_diff);
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
		render.setSeriesPaint(2, Color.BLACK);
		render.setSeriesPaint(3, Color.BLACK);
		render.setSeriesPaint(4, Color.YELLOW);
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
	private static double ecartType(ArrayList<RecordJour> recordList) {
		double somme = 0, moy = 0;
		int taille = 0;
		for (RecordJour jour : recordList) {
			for (Double item : jour.getRecordList()) {
				somme += item;
				taille++;
			}
		}
		moy = somme / taille;
		somme = 0;
		for (RecordJour jour : recordList) {
			for (Double item : jour.getRecordList()) {
				somme += (item - moy) * (item - moy);
			}
		}
		return Math.sqrt(somme / taille);
	}
}
