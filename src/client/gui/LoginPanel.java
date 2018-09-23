package client.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import client.ClientConnection;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel {

	public LoginPanel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setPreferredSize(new Dimension(800, 640));
		
		// Create component
		JButton loginButton = new JButton("Login");
		loginButton.setPreferredSize(new Dimension(100, 24));
		JButton testButton = new JButton("Test (Send msg)");
		testButton.setPreferredSize(new Dimension(100, 24));

		// Add component
		add(loginButton);
		add(testButton);

		// Add listener
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// do something
				MainFrame.getInstance().login();
			}
		});

		testButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientConnection.getInstance().sendMsg("something");
			}
		});
	}
}