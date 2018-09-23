package client.gui;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

import client.RandomUtil;

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

		// Create component
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(100, 600));
		boardPanel = new BoardPanel();

		// Add component
		add(boardPanel);
		add(buttonPanel);

		RandomUtil randomUtil = new RandomUtil();
		generateButton(randomUtil.generateCharacters(6));
	}

	private void generateButton(char[] randomAlphabet) {
		for (char alphabet : randomAlphabet) {
			JButton jButton = new JButton(Character.toString(alphabet));
			jButton.addActionListener(boardPanel.new ButtonActionListener());
			buttonPanel.add(jButton);
		}
	}
}