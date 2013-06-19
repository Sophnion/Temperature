package methodes.regime;

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
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import controller.MapController;

/**
 * The Class RegimeSaisonnier1.
 */
public class RegimeSaisonnier1 {

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
		if (station != null) {
			ArrayList<Chronique> chroniqueList = station.getChroniqueList();
			if (chroniqueList.isEmpty() == false) {
				boolean firsttime = true;
				int mois = 0, annee = 0;
				double somme = 0, nb = 0;
				TimeSeries data = null;
				for (Chronique ch : chroniqueList) {
					if (ch.getAnnee() != annee) {
						if (firsttime == true)
							firsttime = false;
						else {
							dataset.addSeries(data);
						}
						data = new TimeSeries("Régime Saisonnier Année "
								+ ch.getAnnee());
						mois = ch.getMois();
						annee = ch.getAnnee();
						somme = ch.getTemperature();
						nb = 1;
					} else if (ch.getMois() != mois) {
						data.addOrUpdate(new Month(mois, 1990), somme / nb);
						mois = ch.getMois();
						somme = ch.getTemperature();
						nb = 1;
					} else {
						somme += ch.getTemperature();
						nb++;
					}
				}
				dataset.addSeries(data);
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
				"Mois", // TimeAxisLabel
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
		numberaxis.setRange(DatasetUtilities.findRangeBounds(dataset));
		dateaxis.setDateFormatOverride(new SimpleDateFormat("MMM"));

		XYLineAndShapeRenderer render = new XYLineAndShapeRenderer();
		render.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
				"{0}-{1} {2}", new SimpleDateFormat("MMM"), new DecimalFormat(
						"0.00")));
		xyplot.setRenderer(render);
		return chart;
	}
}
