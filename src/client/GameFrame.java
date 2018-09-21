package client;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


@SuppressWarnings("serial")
public class GameFrame extends JFrame {

	private JPanel buttonPanel;
	private GamePanel gamePanel;

	public GameFrame() {
		this.setTitle("Scrabble");
		this.setSize(800, 640);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) ((screenSize.getWidth() - this.getWidth()) / 2),
				(int) ((screenSize.getHeight() - this.getHeight()) / 2));

		JPanel windowPanel = new JPanel();
		windowPanel.setPreferredSize(new Dimension(800, 640));

		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(100, 600));

		gamePanel = new GamePanel();

		windowPanel.add(gamePanel);
		windowPanel.add(buttonPanel);
		this.add(windowPanel);

		RandomUtil randomUtil = new RandomUtil();
		generateButton(randomUtil.generateCharacters(6));
	}

	private void generateButton(char[] randomAlphabet) {
		for (char alphabet : randomAlphabet) {
			JButton jButton = new JButton(Character.toString(alphabet));
			jButton.addActionListener(gamePanel.new ButtonActionListener());
			buttonPanel.add(jButton);
		}
	}
}