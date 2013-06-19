package model;

/**
 * The Class Chronique.
 */
public class Chronique {

	private int id_station; // 1

	private int id_point_de_mesure; // 2

	private int id_operation_mesure; // 3

	private int id_serie_de_mesure;// 4

	private double temperature;// 6

	private int annee;// 7

	private int mois;// 8

	private int jour;// 9

	private int heure;// 10

	private int minutes;// 11

	private int secondes;// 12

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Chronique [id_station=" + id_station + ", temperature="
				+ temperature + ", annee=" + annee + ", mois=" + mois
				+ ", jour=" + jour + ", heure=" + heure + ", minutes="
				+ minutes + ", secondes=" + secondes + "]";
	}

	/**
	 * Instantiates a new chronique.
	 * 
	 * @param id_station
	 *            the id_station
	 * @param id_point_de_mesure
	 *            the id_point_de_mesure
	 * @param id_operation_mesure
	 *            the id_operation_mesure
	 * @param id_serie_de_mesure
	 *            the id_serie_de_mesure
	 * @param d
	 *            the d
	 * @param annee
	 *            the annee
	 * @param mois
	 *            the mois
	 * @param jour
	 *            the jour
	 * @param heure
	 *            the heure
	 * @param minutes
	 *            the minutes
	 * @param secondes
	 *            the secondes
	 */
	public Chronique(int id_station, int id_point_de_mesure,
			int id_operation_mesure, int id_serie_de_mesure, double d,
			int annee, int mois, int jour, int heure, int minutes, int secondes) {
		this.id_station = id_station;
		this.id_point_de_mesure = id_point_de_mesure;
		this.id_operation_mesure = id_operation_mesure;
		this.id_serie_de_mesure = id_serie_de_mesure;
		this.temperature = d;
		this.annee = annee;
		this.mois = mois;
		this.jour = jour;
		this.heure = heure;
		this.minutes = minutes;
		this.secondes = secondes;
	}

	/**
	 * Gets the id_station.
	 * 
	 * @return the id_station
	 */
	public int getId_station() {
		return id_station;
	}

	/**
	 * Sets the id_station.
	 * 
	 * @param id_station
	 *            the new id_station
	 */
	public void setId_station(int id_station) {
		this.id_station = id_station;
	}

	/**
	 * Gets the id_point_de_mesure.
	 * 
	 * @return the id_point_de_mesure
	 */
	public int getId_point_de_mesure() {
		return id_point_de_mesure;
	}

	/**
	 * Sets the id_point_de_mesure.
	 * 
	 * @param id_point_de_mesure
	 *            the new id_point_de_mesure
	 */
	public void setId_point_de_mesure(int id_point_de_mesure) {
		this.id_point_de_mesure = id_point_de_mesure;
	}

	/**
	 * Gets the id_operation_mesure.
	 * 
	 * @return the id_operation_mesure
	 */
	public int getId_operation_mesure() {
		return id_operation_mesure;
	}

	/**
	 * Sets the id_operation_mesure.
	 * 
	 * @param id_operation_mesure
	 *            the new id_operation_mesure
	 */
	public void setId_operation_mesure(int id_operation_mesure) {
		this.id_operation_mesure = id_operation_mesure;
	}

	/**
	 * Gets the id_serie_de_mesure.
	 * 
	 * @return the id_serie_de_mesure
	 */
	public int getId_serie_de_mesure() {
		return id_serie_de_mesure;
	}

	/**
	 * Sets the id_serie_de_mesure.
	 * 
	 * @param id_serie_de_mesure
	 *            the new id_serie_de_mesure
	 */
	public void setId_serie_de_mesure(int id_serie_de_mesure) {
		this.id_serie_de_mesure = id_serie_de_mesure;
	}

	/**
	 * Gets the temperature.
	 * 
	 * @return the temperature
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * Sets the temperature.
	 * 
	 * @param temperature
	 *            the new temperature
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * Gets the annee.
	 * 
	 * @return the annee
	 */
	public int getAnnee() {
		return annee;
	}

	/**
	 * Sets the annee.
	 * 
	 * @param annee
	 *            the new annee
	 */
	public void setAnnee(int annee) {
		this.annee = annee;
	}

	/**
	 * Gets the mois.
	 * 
	 * @return the mois
	 */
	public int getMois() {
		return mois;
	}

	/**
	 * Sets the mois.
	 * 
	 * @param mois
	 *            the new mois
	 */
	public void setMois(int mois) {
		this.mois = mois;
	}

	/**
	 * Gets the jour.
	 * 
	 * @return the jour
	 */
	public int getJour() {
		return jour;
	}

	/**
	 * Sets the jour.
	 * 
	 * @param jour
	 *            the new jour
	 */
	public void setJour(int jour) {
		this.jour = jour;
	}

	/**
	 * Gets the heure.
	 * 
	 * @return the heure
	 */
	public int getHeure() {
		return heure;
	}

	/**
	 * Sets the heure.
	 * 
	 * @param heure
	 *            the new heure
	 */
	public void setHeure(int heure) {
		this.heure = heure;
	}

	/**
	 * Gets the minutes.
	 * 
	 * @return the minutes
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * Sets the minutes.
	 * 
	 * @param minutes
	 *            the new minutes
	 */
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	/**
	 * Gets the secondes.
	 * 
	 * @return the secondes
	 */
	public int getSecondes() {
		return secondes;
	}

	/**
	 * Sets the secondes.
	 * 
	 * @param secondes
	 *            the new secondes
	 */
	public void setSecondes(int secondes) {
		this.secondes = secondes;
	}

}
