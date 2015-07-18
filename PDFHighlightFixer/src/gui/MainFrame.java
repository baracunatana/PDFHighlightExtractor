package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.Extractor;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -5298394413256567203L;
	private FilePanel inputPanel;
	private FilePanel outputPanel;
	private ProgressPanel progressPanel;
	private JButton runButton;
	private JButton cancelButton;
	private Extractor ext;
	private OptionPanel options;

	public MainFrame() {
		setTitle("PDF Highlight Extractor");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLayout(new GridBagLayout());
		inputPanel = new FilePanel("Input file", true);
		outputPanel = new FilePanel("Output file", false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridwidth = 2;
		add(inputPanel, gbc);
		gbc.gridy++;
		add(outputPanel, gbc);

		gbc.gridy++;
		options = new OptionPanel();
		add(options, gbc);

		progressPanel = new ProgressPanel();
		gbc.gridy++;
		add(progressPanel, gbc);

		gbc.gridwidth = 1;
		runButton = new JButton("Run script");
		gbc.gridy++;
		add(runButton, gbc);

		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (inputPanel.getFile() == null || outputPanel.getFile() == null) {
					completeProcess("Please select the input and output files");
					return;
				}
				if (verifyOutputFile()) {
					try {
						int from = Integer.parseInt(options.getStart());
						int to = options.getEnd().equals("end") ? Extractor.FINISH : Integer.parseInt(options.getEnd());

						ext = new Extractor(inputPanel.getFile(), outputPanel.getFile(), options.getIncludeHighlights(),
								options.getIncludeUnderlines(), options.getOverwriteHighlights(),
								options.getOverwriteUnderlines(), from, to);
						ext.addProgressListener(new IProgressListener() {
							public void advanceProcess(int nPages, int currentPage) {
								progressPanel.updateProgress(currentPage, nPages);
							}

							@Override
							public void finishProcess(String msg) {
								completeProcess(msg);
							}
						});
						startProcess();
						new Thread(ext).start();
					} catch (NumberFormatException exc) {
						completeProcess("Please provide a suitable page interval");
					}
				}
			}
		});

		cancelButton = new JButton("Cancel");
		cancelButton.setEnabled(false);
		gbc.gridx++;
		add(cancelButton, gbc);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ext != null)
					ext.setStopped(true);
			}
		});

		pack();
	}

	public boolean verifyOutputFile() {
		if (outputPanel.getFile().exists()) {
			int resp = JOptionPane.showConfirmDialog(this, "Output file already exits. Do you want to overwrite it?");
			return resp == JOptionPane.YES_OPTION;
		}
		return true;
	}

	public void startProcess() {
		inputPanel.setEnabled(false);
		outputPanel.setEnabled(false);
		runButton.setEnabled(false);
		cancelButton.setEnabled(true);
	}

	public void completeProcess(String msg) {
		JOptionPane.showMessageDialog(this, msg);
		progressPanel.reset();
		inputPanel.setEnabled(true);
		outputPanel.setEnabled(true);
		runButton.setEnabled(true);
		cancelButton.setEnabled(false);
	}

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
	}

}
