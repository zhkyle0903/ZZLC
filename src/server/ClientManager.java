package server;

import java.util.ArrayList;
import java.util.List;

import common.UserInfo;

public class ClientManager {
	private static ClientManager instance;
	private List<ClientConnection> connectedClients;
	private List<ClientConnection> inRoomClients;

	private ClientManager() {
		connectedClients = new ArrayList<>();
		inRoomClients = new ArrayList<>();
	}

	public static synchronized ClientManager getInstance() {
		if (instance == null) {
			instance = new ClientManager();
		}
		return instance;
	}

	public synchronized void clientConnected(ClientConnection clientConnection) {
		connectedClients.add(clientConnection);
		String userinfolist = UserInfo.getUserInfoListJsonString();
		this.broadcast(userinfolist);
		System.out.println("Broadcast complete");
	}

	public synchronized void clientDisconnected(ClientConnection clientConnection) {
		//remove the disconnected user info from the user info list 
		if(UserInfo.getUserInfoList().remove(clientConnection.getUserInfo())==true)
			System.out.println("Removing disconnected user from user info list success. Broadcast the change to all users");
		String userinfolist = UserInfo.getUserInfoListJsonString();
		connectedClients.remove(clientConnection);
		this.broadcast(userinfolist);
		System.out.println("Broadcast complete");
	}
	
	public synchronized void broadcast(String msg) {
		// Broadcast the client message to all clients connected to the server
		for (ClientConnection clientConnection : connectedClients) {
			clientConnection.write(msg);
		}
	}

	public synchronized void broadcast(String msg, List<ClientConnection> specificList) {
		// Broadcast the client message to a specific list of clients connected to the server
		for (ClientConnection clientConnection : specificList) {
			clientConnection.write(msg);
			System.out.println("Broadcast to "+clientConnection.getUserInfo().getUsername());
		}
	}

	public synchronized List<ClientConnection> getConnectedClients() {
		return connectedClients;
	}
	
	public synchronized List<ClientConnection> getInRoomClients(){
		return inRoomClients;
	}
	
}