package client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import common.GameBoard;

import static javax.swing.JOptionPane.showMessageDialog;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel {

	private GameBoard gameBoard;
	private int fontSize;
	private int width;
	private int height;
	private int currentColumn;
	private int currentRow;

	public BoardPanel() {
		this.setPreferredSize(new Dimension(600, 600));
		this.gameBoard = new GameBoard(20, 20);
		this.addMouseListener(new GamePanelMouseAdapter());
		this.width = 0;
		this.height = 0;
		this.currentColumn = -1;
		this.currentRow = -1;
		this.fontSize = 18;
	}

	public BoardPanel(GameBoard gameBoard) {
		this.setPreferredSize(new Dimension(600, 600));
		this.gameBoard = gameBoard;
		this.addMouseListener(new GamePanelMouseAdapter());
		this.width = 0;
		this.height = 0;
		this.currentColumn = -1;
		this.currentRow = -1;
		this.fontSize = 18;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);

		width = (this.getWidth() / gameBoard.getCOLUMNS()) * gameBoard.getCOLUMNS();
		height = (this.getHeight() / gameBoard.getROWS()) * gameBoard.getROWS();
		int slotWidth = width / gameBoard.getCOLUMNS();
		int slotHeight = height / gameBoard.getROWS();

		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2d.setColor(Color.BLACK);

		for (int x = 0; x <= width; x += slotWidth) {
			g2d.setStroke(new BasicStroke(1));
			g2d.drawLine(x, 0, x, height);
		}
		for (int y = 0; y <= height; y += slotHeight) {
			g2d.setStroke(new BasicStroke(1));
			g2d.drawLine(0, y, width, y);
		}

		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
		g2d.setFont(font);
		FontRenderContext fContext = g2d.getFontRenderContext();
		for (int row = 0; row < gameBoard.getROWS(); row++) {
			for (int column = 0; column < gameBoard.getCOLUMNS(); column++) {
				if (!gameBoard.isSlotAvailable(row, column)) {
					int textWidth = (int) font
							.getStringBounds(gameBoard.getValue(row, column), fContext).getWidth();
					int textHeight = (int) font
							.getStringBounds(gameBoard.getValue(row, column), fContext)
							.getHeight();
					g2d.drawString(gameBoard.getValue(row, column),
							(column * slotWidth) + ((slotWidth / 2) - (textWidth / 2)),
							(row * slotHeight) + ((slotHeight / 2) + (textHeight / 3)));
				}
			}
		}
		if (currentColumn != -1 && currentRow != -1) {
			g2d.setColor(new Color(0.0f, 0.0f, 1.0f, 0.3f));
			g2d.fillRect(currentColumn * slotWidth, currentRow * slotHeight, slotWidth,
					slotHeight);
		}
	}

	public boolean messageFromActionListener(String value) {
		if (gameBoard.makeMove(currentRow, currentColumn, value)) {
			repaint();
			MainFrame.getInstance().generateButton(gameBoard.getWord(currentRow, currentColumn));
			return false;
		}
		return true;
	}

	public class ButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton jButton = ((JButton) e.getSource());
			jButton.setVisible(messageFromActionListener(jButton.getText()));
		}
	}

	public class ComboBoxActionListener implements ActionListener {
		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox<String> jComboBox = (JComboBox<String>) e.getSource();
			if (messageFromActionListener((String) jComboBox.getSelectedItem())) {
				showMessageDialog(null, "Tile invalid");
			}
		}
	}

	private class GamePanelMouseAdapter extends MouseInputAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				int slotWidth = width / gameBoard.getCOLUMNS();
				int slotHeight = height / gameBoard.getROWS();
				currentRow = e.getY() / slotHeight;
				currentColumn = e.getX() / slotWidth;
				e.getComponent().repaint();
			}
		}
	}

}
