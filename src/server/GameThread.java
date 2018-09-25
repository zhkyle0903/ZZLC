package server;

import java.util.ArrayList;
import java.util.List;

public class GameThread extends Thread {

	private static GameThread instance;
	private List<ClientConnection> clients;

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
		if (!status) {
			// do something (Game start)
		} else {
			// already started
		}
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}