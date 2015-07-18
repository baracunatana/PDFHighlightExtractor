package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class FilePanel extends JPanel {

	private static final long serialVersionUID = 7024374596039638223L;
	private JLabel filePath;
	private JButton fileChooserButton;
	private File file;

	public FilePanel(String title, final boolean openMode) {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(title));

		filePath = new JLabel("");
		add(filePath, BorderLayout.CENTER);

		URL in = getClass().getResource("/icon/open.png");
		fileChooserButton = new JButton(new ImageIcon(in));
		add(fileChooserButton, BorderLayout.EAST);
		fileChooserButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				FileFilter ff = new FileFilter() {

					public String getDescription() {
						return "PDF Documents (*.pdf)";
					}

					public boolean accept(File f) {
						if (f.isDirectory())
							return true;
						return f.getName().toLowerCase().endsWith(".pdf");
					}
				};
				jfc.addChoosableFileFilter(ff);
				jfc.setFileFilter(ff);
				int response;
				if (openMode)
					response = jfc.showOpenDialog(null);
				else
					response = jfc.showSaveDialog(null);
				if (response == JFileChooser.APPROVE_OPTION) {
					file = jfc.getSelectedFile();
					filePath.setText(file.getPath());
				}
			}
		});
	}

	public File getFile() {
		return file;
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		fileChooserButton.setEnabled(enabled);
	}

}
