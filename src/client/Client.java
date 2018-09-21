package client;

import javax.swing.SwingUtilities;

public class Client {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GameFrame frame = new GameFrame();
				frame.setVisible(true);
			}
		});
	}
}