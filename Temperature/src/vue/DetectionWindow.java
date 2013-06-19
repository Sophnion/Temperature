package vue;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.RefineryUtilities;

/**
 * The Class DetectionWindow.
 */
public class DetectionWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private ChartPanel chartPanel;

	/**
	 * Instantiates a new detection window.
	 * 
	 * @param name
	 *            the name
	 * @param chart
	 *            the chart
	 */
	public DetectionWindow(String name, JFreeChart chart) {
		super(name);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		// chartPanel.setMouseZoomable(true, true);
		// chartPanel.setMouseWheelEnabled(true);

		this.setContentPane(chartPanel);
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
	}
}
