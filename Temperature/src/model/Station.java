package model;

import java.util.ArrayList;

/**
 * The Class Station.
 */
public class Station {

	private int idStation;

	private String nameStation;

	private ArrayList<Chronique> chroniqueList;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "Station" + idStation + " [ chronique List ] : \n";
		for (Chronique ch : chroniqueList) {
			result += ch.toString();
		}
		return result;
	}

	/**
	 * Instantiates a new station.
	 */
	public Station() {
		chroniqueList = new ArrayList<Chronique>();
	}

	/**
	 * Ajout chronique.
	 * 
	 * @param chronique
	 *            the chronique
	 */
	public void ajoutChronique(Chronique chronique) {
		chroniqueList.add(chronique);
	}

	/**
	 * Gets the chronique list.
	 * 
	 * @return the chronique list
	 */
	public ArrayList<Chronique> getChroniqueList() {
		return chroniqueList;
	}

	/**
	 * Gets the id station.
	 * 
	 * @return the id station
	 */
	public int getIdStation() {
		return idStation;
	}

	/**
	 * Sets the id station.
	 * 
	 * @param idStation
	 *            the new id station
	 */
	public void setIdStation(int idStation) {
		this.idStation = idStation;
	}

	/**
	 * Gets the name station.
	 * 
	 * @return the name station
	 */
	public String getNameStation() {
		return nameStation;
	}

	/**
	 * Sets the name station.
	 * 
	 * @param nameStation
	 *            the new name station
	 */
	public void setNameStation(String nameStation) {
		this.nameStation = nameStation;
	}

}
