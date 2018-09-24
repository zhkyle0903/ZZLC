package client.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import common.Constants;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {

	private JPanel buttonPanel;
	private BoardPanel boardPanel;

	public GamePanel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		setPreferredSize(new Dimension(800, 640));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Board Panel
		boardPanel = new BoardPanel();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 2;
		add(boardPanel, c);

		// Combo box
		JComboBox<String> comboBox = new JComboBox<>(Constants.CHARACTERS);
		comboBox.setPreferredSize(new Dimension(100, 26));
		comboBox.setSelectedItem(null);
		comboBox.addActionListener(boardPanel.new ComboBoxActionListener());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(comboBox, c);

		// Buttons
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(100, 500));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(buttonPanel, c);
	}

	public JPanel getButtonPanel() {
		return buttonPanel;
	}

//	private void generateButton(char[] randomAlphabet) {
//		for (char alphabet : randomAlphabet) {
//			JButton jButton = new JButton(Character.toString(alphabet));
//			jButton.addActionListener(boardPanel.new ButtonActionListener());
//			buttonPanel.add(jButton);
//		}
//	}
}