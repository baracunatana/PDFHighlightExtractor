package gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressPanel extends JPanel {

	private static final long serialVersionUID = -8474064602947157286L;
	private JProgressBar progressBar;
	private JLabel progressLabel;

	public ProgressPanel() {
		setBorder(BorderFactory.createTitledBorder("Progress"));
		setLayout(new BorderLayout());

		progressBar = new JProgressBar();
		add(progressBar, BorderLayout.CENTER);

		progressLabel = new JLabel("        ");
		add(progressLabel, BorderLayout.EAST);
	}

	public void updateProgress(int currentPage, int nPages) {
		progressLabel.setText(currentPage + "/" + nPages);
		progressBar.setMinimum(0);
		progressBar.setMaximum(nPages);
		progressBar.setValue(currentPage);
	}

	public void reset() {
		progressLabel.setText("        ");
		progressBar.setMinimum(0);
		progressBar.setMaximum(1);
		progressBar.setValue(0);
	}

}
