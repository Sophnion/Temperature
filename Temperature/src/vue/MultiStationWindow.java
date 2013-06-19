package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.geotools.swing.table.FeatureCollectionTableModel;

import controller.ChartController;
import controller.ChartController.ChartType;

/**
 * The Class MultiStationWindow.
 */
public class MultiStationWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	private ChartController chartControl;

	private JButton valider;

	private JButton chroniqueHoraire;

	private JButton conseil;

	private JTable table;

	private JPanel pan;

	/**
	 * Instantiates a new multi station window.
	 * 
	 * @param model
	 *            the model
	 * @param idStation
	 *            the id station
	 */
	public MultiStationWindow(FeatureCollectionTableModel model,
			final int idStation) {

		chartControl = new ChartController();

		this.setMinimumSize(new Dimension(600, 200));
		this.setTitle("Tableau des stations à 50km de rayon");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		valider = new JButton("Valider la station à comparer");
		conseil = new JButton("Conseil");
		chroniqueHoraire = new JButton("Chronique Horaire Multi-Station");

		table = new JTable();
		table.setModel(new DefaultTableModel(5, 5));
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setPreferredScrollableViewportSize(new Dimension(670, 300));
		table.setSelectionBackground(Color.PINK);
		table.setModel(model);
		// hide les colonnes inutiles
		for (int i = 0; i < 27; i++) {
			if (i != 3 && i != 6 && i != 9 && i != 22 && i != 23 && i != 25
					&& i != 26) {
				JTableHeader header = table.getTableHeader();
				TableColumn column = table.getColumnModel().getColumn(i);
				header.setResizingColumn(column);
				column.setMinWidth(-1);
				column.setMaxWidth(-1);
			}
		}
		JScrollPane scrollPane = new JScrollPane(table);

		chroniqueHoraire.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				int id = (int) table.getValueAt(row, 3);
				chartControl.createChartWindow(id, ChartType.ChroniqueHoraire);
			}
		});

		valider.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				int id = (int) table.getValueAt(row, 3);
				chartControl.createDetectionWindow(idStation, id,
						ChartType.DeltaJournalier);
			}
		});

		pan = new JPanel();
		pan.add(conseil);
		pan.add(chroniqueHoraire);
		pan.add(valider);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(pan, BorderLayout.SOUTH);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);

		this.pack();
		this.setVisible(true);
	}

}
