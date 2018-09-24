package server;

import java.util.ArrayList;
import java.util.List;

public class GameThread extends Thread {

	private static GameThread instance;
	private List<ClientConnection> clients;
	private GameBoard gameBoard;
	private ClientConnection currentPlayer;

	private boolean status;

	private GameThread() {
		this.clients = new ArrayList<>();
		this.status = false;
	}

	public static synchronized GameThread getInstance() {
		if (instance == null) {
			instance = new GameThread();
		}
		return instance;
	}

	public synchronized void clientAdded(ClientConnection clientConnection) {
		// do something (broadcast)
		clients.add(clientConnection);
	}

	public synchronized void clientRemove(ClientConnection clientConnection) {
		// do something (broadcast)
		clients.remove(clientConnection);
	}

	@Override
	public synchronized void run() {
		this.status = true;
		// do something (Game start)
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public void setGameBoard(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}

	public ClientConnection getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(ClientConnection currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
}