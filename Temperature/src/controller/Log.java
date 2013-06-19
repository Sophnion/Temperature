package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * The Class Log.
 */
public class Log {

	private static String newLine = System.getProperty("line.separator");

	/**
	 * Inits the Log.
	 */
	public static void init() {
		new File(System.getProperty("user.dir") + "/" + "Log").mkdir();
		new File(System.getProperty("user.dir") + "/" + "mesures_corrige")
				.mkdir();
		ecrire("Log/Log.txt", "FICHIER LOG RNT : "
				+ Calendar.getInstance().getTime() + newLine
				+ "------------------------------------------------" + newLine);
	}

	/**
	 * Ecrire.
	 * 
	 * @param nomFicher
	 *            the nom ficher
	 * @param texte
	 *            the texte
	 */
	public static void ecrire(String nomFicher, String texte) {
		String adressedufichier = System.getProperty("user.dir") + "/"
				+ nomFicher;
		try {

			FileWriter fw = new FileWriter(adressedufichier, true);
			BufferedWriter output = new BufferedWriter(fw);
			output.write(texte);
			output.flush();
			output.close();
		} catch (IOException ioe) {
			System.out.print("Erreur d'ouverture: ");
			ioe.printStackTrace();
		}
	}
}
