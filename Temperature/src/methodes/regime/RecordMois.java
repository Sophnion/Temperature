package methodes.regime;

import java.util.ArrayList;

/**
 * The Class RecordMois.
 */
public class RecordMois {

	private double sommeMois;

	private int tailleMois;

	private double[] sommeHeure;

	private int[] tailleHeure;

	private ArrayList<Double> record;

	/**
	 * Instantiates a new record mois.
	 */
	public RecordMois() {
		sommeMois = 0;
		tailleMois = 0;
		sommeHeure = new double[24];
		tailleHeure = new int[24];
		record = new ArrayList<Double>();
	}

	/**
	 * Ajout record.
	 * 
	 * @param heure
	 *            the heure
	 * @param temperature
	 *            the temperature
	 */
	public void ajoutRecord(int heure, double temperature) {
		sommeMois += temperature;
		tailleMois++;
		sommeHeure[heure] += temperature;
		tailleHeure[heure]++;
		record.add(temperature);
	}

	/**
	 * Gets the moyen mois.
	 * 
	 * @return the moyen mois
	 */
	public double getMoyenMois() {
		return sommeMois / tailleMois;
	}

	/**
	 * Gets the moyen heure.
	 * 
	 * @param heure
	 *            the heure
	 * @return the moyen heure
	 */
	public double getMoyenHeure(int heure) {
		return sommeHeure[heure] / tailleHeure[heure];
	}

	/**
	 * Gets the record.
	 * 
	 * @return the record
	 */
	public ArrayList<Double> getRecord() {
		return record;
	}

}
