package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;

import model.Chronique;
import model.Station;

/**
 * The Class StationController.
 */
public class StationController {

	private JFileChooser chooseFile;

	private File[] file;

	private FileFilter filterTxt;

	/**
	 * Instantiates a new station controller.
	 */
	public StationController() {
		chooseFile = new JFileChooser();
		filterTxt = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return false;
				}
				String ext = null;
				String name = pathname.getName();
				int i = name.lastIndexOf('.');
				if (i > 0 && i < name.length() - 1) {
					ext = name.substring(i + 1).toLowerCase();
				}
				if (ext != null && ext.equals("txt"))
					return true;
				return false;
			}
		};
	}

	/**
	 * Choose files.
	 * 
	 * @return true, if successful
	 */
	public boolean chooseFiles() {
		chooseFile.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooseFile.setMultiSelectionEnabled(true);
		int result = chooseFile.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			file = chooseFile.getSelectedFiles();
			return true;
		}
		return false;
	}

	/**
	 * Charge station brut.
	 */
	public void chargeStationBrut() {
		for (int i = 0; i < file.length; i++) {
			if (file[i].isDirectory() == true) {
				File[] fichiers = file[i].listFiles(filterTxt);
				for (int j = 0; j < fichiers.length; j++) {
					analyse_fichier(fichiers[j]);
				}
			} else {
				analyse_fichier(file[i]);
			}
		}
	}

	/**
	 * Analyse_fichier.
	 * 
	 * @param fileAnalyse
	 *            the file analyse
	 */
	private void analyse_fichier(File fileAnalyse) {
		BufferedReader lecteurAvecBuffer = null;
		String ligne = null;
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(fileAnalyse));
		} catch (FileNotFoundException exc) {
			System.out.println("Erreur d'ouverture");
		}
		try {
			String[] chaineEclate = null;
			ligne = lecteurAvecBuffer.readLine();
			while ((ligne = lecteurAvecBuffer.readLine()) != null) {
				String eclate = ligne;
				chaineEclate = eclate.split("\t");
				Chronique ch = new Chronique(Integer.valueOf(chaineEclate[0]),
						Integer.valueOf(chaineEclate[1]),
						Integer.valueOf(chaineEclate[2]),
						Integer.valueOf(chaineEclate[3]),
						Double.parseDouble(chaineEclate[5]),
						Integer.valueOf(chaineEclate[6]),
						Integer.valueOf(chaineEclate[7]),
						Integer.valueOf(chaineEclate[8]),
						Integer.valueOf(chaineEclate[9]),
						Integer.valueOf(chaineEclate[10]),
						Integer.valueOf(chaineEclate[11]));
				Station st = MapController.getStationList().getStationById(
						ch.getId_station());
				st.setIdStation(ch.getId_station());
				st.ajoutChronique(ch);
				eclate = "";
			}
			lecteurAvecBuffer.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * Charge station modifie.
	 */
	public void chargeStationModifie() {
		// TODO chemin relative ou chemin absolu
		File dir = new File(
				"/home/yuan/Documents/Stage_Temperature/Eclipse/Temperature/mesures_corrige/");
		File[] fs = dir.listFiles();
		for (int j = 0; j < fs.length; j++) {
			System.out.println(fs[j].getName());
		}
	}

}
