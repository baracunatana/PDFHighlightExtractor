package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OptionPanel extends JPanel {

	private static final long serialVersionUID = -9082721230516495624L;
	private JCheckBox includeHighlights;
	private JCheckBox includeUnderlines;
	private JCheckBox overWriteHighlights;
	private JCheckBox overWriteUnderlines;
	private JTextField from;
	private JTextField to;

	public OptionPanel() {
		setBorder(BorderFactory.createTitledBorder("Options"));
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		add(new JLabel("Pages: "), gbc);

		from = new JTextField("1", 4);
		gbc.gridx++;
		add(from, gbc);

		gbc.gridx++;
		add(new JLabel("-"));

		to = new JTextField("end", 4);
		gbc.gridx++;
		add(to, gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 4;
		includeHighlights = new JCheckBox("Include highlights");
		includeHighlights.setSelected(true);
		add(includeHighlights, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 4;
		overWriteHighlights = new JCheckBox("Overwrite highlights content");
		add(overWriteHighlights, gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 4;
		includeUnderlines = new JCheckBox("Include underlines");
		add(includeUnderlines, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 4;
		overWriteUnderlines = new JCheckBox("Overwrite underlines content");
		add(overWriteUnderlines, gbc);
	}

	public boolean getIncludeHighlights() {
		return includeHighlights.isSelected();
	}

	public boolean getIncludeUnderlines() {
		return includeUnderlines.isSelected();
	}

	public boolean getOverwriteHighlights() {
		return overWriteHighlights.isSelected();
	}

	public boolean getOverwriteUnderlines() {
		return overWriteUnderlines.isSelected();
	}

	public String getStart() {
		return from.getText();
	}

	public String getEnd() {
		return to.getText();
	}

}
