package model;

import java.util.ArrayList;

/**
 * The Class Map.
 */
public class Map {

	private ArrayList<Station> stationList;

	/**
	 * Instantiates a new map.
	 */
	public Map() {
		stationList = new ArrayList<Station>();
	}

	/**
	 * Ajout station.
	 * 
	 * @param station
	 *            the station
	 */
	public void ajoutStation(Station station) {
		stationList.add(station);
	}

	/**
	 * Gets the name by id.
	 * 
	 * @param idStation
	 *            the id station
	 * @return the name by id
	 */
	public String getNameById(int idStation) {
		for (Station st : stationList) {
			if (st.getIdStation() == idStation)
				return st.getNameStation();
		}
		return null;
	}

	/**
	 * Gets the station list.
	 * 
	 * @return the station list
	 */
	public ArrayList<Station> getStationList() {
		return stationList;
	}

	/**
	 * Gets the station by id.
	 * 
	 * @param id
	 *            the id
	 * @return the station by id
	 */
	public Station getStationById(int id) {
		for (Station st : stationList) {
			if (st.getIdStation() == id)
				return st;
		}
		return null;
	}

}
