package client.gui;

public class GameBoard {

	protected String[][] board;
	private final int ROWS;
	private final int COLUMNS;

	public GameBoard(int rows, int columns) {
		this.ROWS = rows;
		this.COLUMNS = columns;
		this.board = new String[ROWS][COLUMNS];
		initializeBoard();
	}

	public GameBoard(GameBoard gameBoard) {
		this.ROWS = gameBoard.ROWS;
		this.COLUMNS = gameBoard.COLUMNS;
		this.board = new String[ROWS][COLUMNS];
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLUMNS; c++) {
				board[r][c] = gameBoard.board[r][c];
			}
		}
	}

	private void initializeBoard() {
		for (int r = 0; r < this.ROWS; r++) {
			for (int c = 0; c < this.COLUMNS; c++) {
				this.board[r][c] = "";
			}
		}
	}

	public boolean boardFull() {
		for (int r = 0; r < this.ROWS; r++) {
			for (int c = 0; c < this.COLUMNS; c++) {
				if (this.board[r][c].equals(""))
					return false;
			}
		}
		return true;
	}

	public boolean isSlotAvailable(int row, int col) {
		return (this.inRange(row, col) && this.board[row][col].equals(""));
	}

	public int getROWS() {
		return ROWS;
	}

	public int getCOLUMNS() {
		return COLUMNS;
	}

	public boolean makeMove(int row, int col, String value) {
		if (this.isSlotAvailable(row, col)) {
			this.board[row][col] = value;
			return true;
		}
		return false;
	}

	public String getValue(int row, int col) {
		if (this.inRange(row, col)) {
			return this.board[row][col];
		}
		return "";
	}

	public String[][] getBoard() {
		return this.board;
	}

	private boolean inRange(int row, int col) {
		return row >= 0 && col >= 0 && row <= this.ROWS && col <= this.COLUMNS;
	}
}