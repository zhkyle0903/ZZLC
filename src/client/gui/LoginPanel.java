package client.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONException;

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
		JLabel enterUsername = new JLabel("Enter username:");
		enterUsername.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		enterUsername.setPreferredSize(new Dimension(100, 24));
		JTextField usernameEntry = new JTextField();
		usernameEntry.setPreferredSize(new Dimension(100, 24));
		

		// Add component
		add(loginButton);
		add(testButton);
		add(enterUsername);
		add(usernameEntry);
		
		// Add listener
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//create a json package containing request type and username
				String username = usernameEntry.getText();
				if(isValidUsername(username)==true) {
					try {
						if(ClientConnection.getInstance().tryToLogin(username)==true) {
							MainFrame.getInstance().login();
						}
						else
						{
							ErrorFrame errorWindow = new ErrorFrame("Login Failed: Username is already being used.");
							errorWindow.setVisible(true);
						}
					}
						catch(JSONException | IOException e1)
						{
							e1.printStackTrace();
							ErrorFrame errorWindow = new ErrorFrame(e1.toString());
							errorWindow.setVisible(true);
						}
				}
				else
				{
					ErrorFrame errorWindow = new ErrorFrame("Username invalid.");
					errorWindow.setVisible(true);
				}
			}
		});

		testButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientConnection.getInstance().sendMsg("something");
				
			}
		});
	}

	public boolean isValidUsername(String username) {
		for(int i=0;i<username.length();i++)
		{
			if(Character.isDigit(username.charAt(i))||Character.isLetter(username.charAt(i))==false)
				return false;
		}
		return true;
	}
}