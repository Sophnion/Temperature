package vue;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.geotools.filter.text.cql2.CQLException;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.JMapPane;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;

import controller.ChartController;
import controller.ChartController.ChartType;
import controller.MapController;
import controller.StationController;

/**
 * The Class MainWindow.
 */
public class MainWindow extends JMapFrame {

	private static final long serialVersionUID = 1L;

	private int id;

	private MapController mapControl;

	private StationController stationControl;

	private ChartController chartControl;

	private ChargeMapThread chargeMapThread = null;

	private ChargeDonneeBrut chargeDBThread = null;

	private Runnable run = null;

	/**
	 * The Class ChargeMapThread.
	 */
	private class ChargeMapThread extends Thread {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				mapControl.chargeShapeFile();
				setMapContent(mapControl.getMap());
				// TODO
			} catch (Exception e) {
				e.printStackTrace();
			}
			SwingUtilities.invokeLater(run);
		}
	}

	/**
	 * The Class ChargeDonneeBrut.
	 */
	private class ChargeDonneeBrut extends Thread {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			stationControl.chargeStationBrut();
			SwingUtilities.invokeLater(run);
		}
	}

	/**
	 * Instantiates a new main window.
	 */
	public MainWindow() {
		final JProgressBar progressBar = new JProgressBar(0, 100);
		final JToolBar statusBar = new JToolBar();
		mapControl = new MapController();
		stationControl = new StationController();
		chartControl = new ChartController();

		this.setMinimumSize(new Dimension(600, 200));
		this.setTitle("Température de l'eau : détection des erreurs");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setExtendedState(getExtendedState() | MAXIMIZED_BOTH);

		final JLabel status = new JLabel("Bienvenue !");
		final JLabel progression = new JLabel("Progression : ");
		final JLabel progressionfini = new JLabel("100%");
		status.setMinimumSize(new Dimension(150, 20));
		status.setMaximumSize(new Dimension(150, 20));
		statusBar.add(status);
		statusBar.add(progression);
		statusBar.add(progressBar);
		statusBar.add(progressionfini);
		progressBar.setValue(100);
		progressBar.setMaximumSize(new Dimension(200, 20));
		statusBar.setFloatable(false);

		final JLabel stationLabel = new JLabel("   ID Station :");
		final JTextField idStation = new JTextField();
		final JButton stationButton = new JButton("Voir Information");
		final JLabel instruction = new JLabel(
				"Instruction:  Ajout du shapefile -> Ajout des données -> Choix de station");
		idStation.setMaximumSize(new Dimension(70, 25));
		this.enableToolBar(true);
		this.enableTool(Tool.ZOOM, Tool.PAN, Tool.RESET, Tool.POINTER,
				Tool.INFO);
		this.getToolBar().add(stationLabel);
		this.getToolBar().add(idStation);
		this.getToolBar().add(stationButton);
		this.getToolBar().addSeparator();
		this.getToolBar().add(instruction);

		final java.net.URL url = MainWindow.class
				.getResource("/ressource/last.png");
		final JTextArea areaTxt = new JTextArea() {
			private static final long serialVersionUID = 1L;
			final ImageIcon imageIcon = new ImageIcon(url);
			Image image = imageIcon.getImage();
			{
				setOpaque(false);
			}

			@Override
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, this);
				super.paintComponent(g);
			}
		};
		areaTxt.setEditable(false);
		areaTxt.setFont(new java.awt.Font("Dialog", 0, 15));
		areaTxt.setForeground(java.awt.Color.black);
		areaTxt.setPreferredSize(new Dimension(250, 150));
		areaTxt.setVisible(true);
		final JScrollPane scrollPane = new JScrollPane(areaTxt);
		final JPanel panTxt = new JPanel();
		panTxt.setLayout(new BorderLayout());
		panTxt.add(scrollPane);

		final JMenuBar barreMenu = new JMenuBar();

		final JMenu mFichier = new JMenu("Fichier");
		final JMenu mVisu = new JMenu("Visualisation");
		final JMenu mDetec1 = new JMenu("Détection une Station");
		final JMenu mDetec2 = new JMenu("Détection Multi-Stations");
		final JMenu mOutils = new JMenu("Outils");
		final JMenu mAPropos = new JMenu("?");
		barreMenu.add(mFichier);
		barreMenu.add(mVisu);
		barreMenu.add(mDetec1);
		barreMenu.add(mDetec2);
		barreMenu.add(mOutils);
		barreMenu.add(mAPropos);

		final JMenuItem iAjoutVecteur = new JMenuItem("Ajout Shapefile *.shp");
		final JMenu iAjoutChronique = new JMenu("Mode Chargement");
		final JMenuItem iQuitter = new JMenuItem("Quitter");
		mFichier.add(iAjoutVecteur);
		mFichier.add(iAjoutChronique);
		mFichier.addSeparator();
		mFichier.add(iQuitter);
		final JMenuItem mSousMenuChargerList1 = new JMenuItem("Données Brutes");
		final JMenuItem mSousMenuChargerList2 = new JMenuItem(
				"Données Modifiées");
		iAjoutChronique.add(mSousMenuChargerList1);
		iAjoutChronique.add(mSousMenuChargerList2);

		final JMenuItem iVisuGraph0 = new JMenuItem("Chronique Horaire");
		final JMenuItem iVisuGraph1 = new JMenuItem("Chronique Journalière");
		final JMenuItem iVisuGraph2 = new JMenuItem("Régime Saisonnier 1");
		final JMenuItem iVisuGraph3 = new JMenuItem("Régime Saisonnier 2");
		final JMenuItem iVisuGraph4 = new JMenuItem("Régime Horaire Janv/Juil");
		final JMenuItem iVisuGraph5 = new JMenuItem("Régime Horaire Annuel");
		mVisu.add(iVisuGraph0);
		mVisu.add(iVisuGraph1);
		mVisu.add(iVisuGraph2);
		mVisu.add(iVisuGraph3);
		mVisu.add(iVisuGraph4);
		mVisu.add(iVisuGraph5);

		final JMenuItem mDeltaHoraire = new JMenuItem("Delta Horaire");
		mDetec1.add(mDeltaHoraire);

		final JMenuItem ichoixStation = new JMenuItem(
				"Choix de la station à comparer");
		final JMenuItem mDeltaJournalier = new JMenuItem("Delta Journalier");
		mDetec2.add(ichoixStation);
		mDetec2.addSeparator();
		mDetec2.add(mDeltaJournalier);

		final JMenuItem iOutils0 = new JMenuItem("Ouvrir Log");
		mOutils.add(iOutils0);

		final JMenuItem iAPropos = new JMenuItem("Aide .pdf");
		final JMenuItem iAPropos2 = new JMenuItem("A propos de ...");
		mAPropos.add(iAPropos);
		mAPropos.add(iAPropos2);


		run = new Runnable() {// initialisation de run
			@Override
			public void run() {
				progressBar.setIndeterminate(false);
				iAjoutChronique.setEnabled(true);
			}
		};

		/*
		 * Initialisation pour choisir une station
		 */
		getMapPane().setCursorTool(new CursorTool() {
			@Override
			public void onMouseMoved(MapMouseEvent ev) {
				status.setText("X: " + ev.getPoint().x + " Y :"
						+ ev.getPoint().y + "     ");
			}

			@Override
			public void onMouseClicked(MapMouseEvent ev) {
				try {
					String info = mapControl.selectFeatures(ev,
							(JMapPane) this.getMapPane());
					areaTxt.setText("Information de station: \n");
					areaTxt.append(info);
					idStation.setText(mapControl.getSelectFeatureID());
					id = Integer.valueOf(mapControl.getSelectFeatureID());
				} catch (CQLException e) {
					//si on n'a pas reussit à choisir une station
					idStation.setText("");
				}
			}
		});
		this.getToolBar().getComponent(0).addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				getMapPane().setCursorTool(new CursorTool() {
					@Override
					public void onMouseMoved(MapMouseEvent ev) {
						status.setText("X: " + ev.getPoint().x + " Y :"
								+ ev.getPoint().y + "     ");
					}

					@Override
					public void onMouseClicked(MapMouseEvent ev) {
						try {
							String info = mapControl.selectFeatures(ev,
									(JMapPane) this.getMapPane());
							areaTxt.setText("Information de station: \n");
							areaTxt.append(info);
							idStation.setText(mapControl.getSelectFeatureID());
							id = Integer.valueOf(mapControl.getSelectFeatureID());
						} catch (CQLException e) {
							//si on n'a pas reussit à choisir une station
							idStation.setText("");
						}
					}
				});
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});

		/*
		 * Interaction Menu
		 */
		iAjoutVecteur.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mapControl.chooseFile() == true) {
					progressBar.setIndeterminate(true);
					status.setText("Charger Map");
					chargeMapThread = new ChargeMapThread();
					chargeMapThread.start();
				}
			}
		});

		mSousMenuChargerList1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (stationControl.chooseFiles() == true) {
					progressBar.setIndeterminate(true);
					status.setText("Charger Données");
					chargeDBThread = new ChargeDonneeBrut();
					chargeDBThread.start();
				}
			}
		});

		mSousMenuChargerList2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO charge donnee modifie
				stationControl.chargeStationModifie();
			}
		});

		iQuitter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		iVisuGraph0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chartControl.createChartWindow(id, ChartType.ChroniqueHoraire);
			}
		});

		iVisuGraph1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chartControl.createChartWindow(id,
						ChartType.ChroniqueJournalier);
			}
		});

		iVisuGraph2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chartControl.createChartWindow(id, ChartType.RegimeSaisonnier1);
			}
		});

		iVisuGraph3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chartControl.createChartWindow(id, ChartType.RegimeSaisonnier2);
			}
		});

		iVisuGraph4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chartControl.createChartWindow(id,
						ChartType.RegimeHoraireJanvJui);
			}
		});

		iVisuGraph5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chartControl.createChartWindow(id,
						ChartType.RegimeHoraireAnnuel);
			}
		});

		mDeltaHoraire.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chartControl.createDetectionWindow(id, ChartType.DeltaHoraire);
			}
		});

		ichoixStation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mapControl.createMultiStationWindow();
			}
		});

		mDeltaJournalier.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chartControl.createDetectionWindow(id, 1543,
						ChartType.DeltaJournalier);
			}
		});

		iOutils0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File f = new File(System.getProperty("user.dir") + "/"
						+ "Log/Log.txt");
				try {
					Desktop.getDesktop().open(f);
				} catch (IOException e) {
					System.out.println("error Log");
					e.printStackTrace();
				}
			}
		});

		final java.net.URL manuel = MainWindow.class
				.getResource("/ressource/notice.pdf");
		iAPropos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					java.awt.Desktop.getDesktop().open(
							new File(manuel.getPath()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		iAPropos2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				javax.swing.JOptionPane.showMessageDialog(null,
						"@Lacombe Jérémy, @Zhang Yuan, version 2.0");
			}
		});

		stationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				areaTxt.setText("Information de station: \n");
				if (!idStation.getText().isEmpty()) {
					id = Integer.valueOf(idStation.getText());
					try {
						areaTxt.append(mapControl.displayInformation(id,(JMapPane)getMapPane()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		/*
		 * Show Window
		 */
		this.setJMenuBar(barreMenu);
		this.getContentPane().add(panTxt, BorderLayout.EAST);
		this.getContentPane().add(statusBar, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}

}
