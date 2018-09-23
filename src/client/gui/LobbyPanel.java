package client.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LobbyPanel extends JPanel {

	public LobbyPanel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setPreferredSize(new Dimension(800, 640));

		// Create component
		JButton startButton = new JButton("Start Game");
		startButton.setPreferredSize(new Dimension(100, 24));

		// Add component
		add(startButton);

		// Add listener
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// do something
				MainFrame.getInstance().startGame();
			}
		});
	}
}