package client.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static MainFrame instance;
	private LoginPanel loginPanel;
	private LobbyPanel lobbyPanel;
	private GamePanel gamePanel;

	private MainFrame() {
		initialize();
	}

	public static MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}
		return instance;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setTitle("Scrabble");
		this.setSize(800, 640);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) ((screenSize.getWidth() - this.getWidth()) / 2),
				(int) ((screenSize.getHeight() - this.getHeight()) / 2));
		loginPanel = new LoginPanel();
		add(loginPanel);
	}

	public void login() {
		remove(loginPanel);
		lobbyPanel = new LobbyPanel();
		add(lobbyPanel);
		revalidate();
		repaint();
	}

	public void startGame() {
		remove(lobbyPanel);
		gamePanel = new GamePanel();
		add(gamePanel);
		revalidate();
		repaint();
	}

	public void generateButton(String[] words) {
		for (String word : words) {
			JButton jButton = new JButton(word);
			gamePanel.getButtonPanel().add(jButton);
		}
		revalidate();
		repaint();
	}
	
	public LobbyPanel getLobbyPanel() {
		return this.lobbyPanel;
	}
	
}