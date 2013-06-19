package main;

import controller.Log;
import vue.MainWindow;

/**
 * The Class Main.
 */
public class Main {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		new MainWindow();
		Log.init();
	}

}
