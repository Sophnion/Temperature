package controller;

import methodes.DeltaHoraire;
import methodes.DeltaJournalier;
import methodes.chronique.ChroniqueHoraire;
import methodes.chronique.ChroniqueJournalier;
import methodes.regime.RegimeHoraireJJ;
import methodes.regime.RegimeSaisonnier1;
import methodes.regime.RegimeSaisonnier2;
import methodes.regime.RegimeHoraireAnnuel;

import org.jfree.chart.JFreeChart;

import vue.ChartWindow;
import vue.DetectionWindow;


/**
 * The Class ChartController.
 */
public class ChartController {

	
	/**
	 * The Enum ChartType.
	 */
	public enum ChartType {

		ChroniqueJournalier,

		ChroniqueHoraire,

		RegimeSaisonnier1,

		RegimeSaisonnier2,

		RegimeHoraireAnnuel,

		RegimeHoraireJanvJui,

		DeltaHoraire,

		DeltaJournalier
	}

	private JFreeChart chart;

	private String name;

	
	/**
	 * Creates the chart window.
	 * 
	 * @param idStation
	 *            the id station
	 * @param chartType
	 *            the chart type
	 */
	public void createChartWindow(int idStation, ChartType chartType) {
		chart = null;
		if (MapController.getStationList().getStationById(idStation) == null) {
			javax.swing.JOptionPane.showMessageDialog(null,
					"Veuillez d'abord choisir une station");
			return;
		}
		if (MapController.getStationList().getStationById(idStation)
				.getChroniqueList().isEmpty()) {
			javax.swing.JOptionPane.showMessageDialog(null,
					"Veuillez d'abord charger les données pour cette station");
			return;
		}
		switch (chartType) {
		case ChroniqueJournalier:
			chart = ChroniqueJournalier.createChart(idStation);
			name = "Chronique Journalier";
			break;
		case ChroniqueHoraire:
			chart = ChroniqueHoraire.createChart(idStation);
			name = "Chronique Horaire";
			break;
		case RegimeSaisonnier1:
			chart = RegimeSaisonnier1.createChart(idStation);
			name = "Regime Saisonnier 1";
			break;
		case RegimeSaisonnier2:
			chart = RegimeSaisonnier2.createChart(idStation);
			name = "Regime Saisonnier 2";
			break;
		case RegimeHoraireAnnuel:
			chart = RegimeHoraireAnnuel.createChart(idStation);
			name = "Regime Horaire Annuel";
			break;
		case RegimeHoraireJanvJui:
			chart = RegimeHoraireJJ.createChart(idStation);
			name = "Regime Horaire Janvier/Juillet";
			break;
		}
		new ChartWindow(name, chart);
	}

	
	/**
	 * Creates the detection window.
	 * 
	 * @param idStation
	 *            the id station
	 * @param chartType
	 *            the chart type
	 */
	public void createDetectionWindow(int idStation, ChartType chartType) {
		chart = null;
		if (MapController.getStationList().getStationById(idStation) == null) {
			javax.swing.JOptionPane.showMessageDialog(null,
					"Veuillez d'abord choisir une station");
			return;
		}
		if (MapController.getStationList().getStationById(idStation)
				.getChroniqueList().isEmpty()) {
			javax.swing.JOptionPane.showMessageDialog(null,
					"Veuillez d'abord charger les données pour cette station");
			return;
		}
		switch (chartType) {
		case DeltaHoraire:
			chart = DeltaHoraire.createChart(idStation);
			name = "Delta Horaire";
			break;
		}
		new DetectionWindow(name, chart);
	}

	
	/**
	 * Creates the detection window.
	 * 
	 * @param idStation1
	 *            the id station1
	 * @param idStation2
	 *            the id station2
	 * @param chartType
	 *            the chart type
	 */
	public void createDetectionWindow(int idStation1, int idStation2,
			ChartType chartType) {
		chart = null;
		if (MapController.getStationList().getStationById(idStation1) == null
				|| MapController.getStationList().getStationById(idStation2) == null) {
			javax.swing.JOptionPane.showMessageDialog(null,
					"Veuillez d'abord choisir une station");
			return;
		}
		if (MapController.getStationList().getStationById(idStation1)
				.getChroniqueList().isEmpty()
				|| MapController.getStationList().getStationById(idStation2)
						.getChroniqueList().isEmpty()) {
			javax.swing.JOptionPane.showMessageDialog(null,
					"Veuillez d'abord charger les données pour ces stations");
			return;
		}
		switch (chartType) {
		case DeltaJournalier:
			chart = DeltaJournalier.createChart(idStation1, idStation2);
			name = "Delta Journalier";
			break;
		}
		new DetectionWindow(name, chart);
	}
}
