package methodes;

import java.util.ArrayList;

/**
 * The Class RecordJour.
 */
public class RecordJour {

	private ArrayList<Double> recordList;

	/**
	 * Instantiates a new record jour.
	 */
	public RecordJour() {
		recordList = new ArrayList<Double>();
	}

	/**
	 * Ajout record.
	 * 
	 * @param temperature
	 *            the temperature
	 */
	public void ajoutRecord(double temperature) {
		recordList.add(temperature);
	}

	/**
	 * Gets the record list.
	 * 
	 * @return the record list
	 */
	public ArrayList<Double> getRecordList() {
		return recordList;
	}

}
