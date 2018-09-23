package server;

import java.util.ArrayList;
import java.util.List;

public class ClientManager {
	private static ClientManager instance;
	private List<ClientConnection> connectedClients;

	private ClientManager() {
		connectedClients = new ArrayList<>();
	}

	public static synchronized ClientManager getInstance() {
		if (instance == null) {
			instance = new ClientManager();
		}
		return instance;
	}

	public synchronized void clientConnected(ClientConnection clientConnection) {
		// do something (broadcast)
		connectedClients.add(clientConnection);
	}

	public synchronized void clientDisconnected(ClientConnection clientConnection) {
		// do something (broadcast)
		connectedClients.remove(clientConnection);
	}

	public synchronized void processMessage(String msg) {
		// Broadcast the client message to all the clients connected to the server
		for (ClientConnection clientConnection : connectedClients) {
			clientConnection.write(msg);
		}
	}

	public synchronized List<ClientConnection> getConnectedClients() {
		return connectedClients;
	}
}