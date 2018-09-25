package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

import common.Constants;
import common.JsonUtil;

public class ClientConnection extends Thread {

	private Socket clientSocket;
	private int clientNum;
	private BufferedReader reader;
	private BufferedWriter writer;

	// Received message
	private String clientMsg;
	// Unique identification
	private String clientName;
	// Whether in a game or not
	private boolean status;
	// Current Count
	private int clientCount;

	public ClientConnection(Socket clientSocket, int clientNum) {
		try {
			this.clientSocket = clientSocket;
			this.clientNum = clientNum;
			this.reader = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			this.writer = new BufferedWriter(
					new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		JsonUtil jsonUtil = new JsonUtil();
		try {
			System.out.println(Thread.currentThread().getName()
					+ " - Reading messages from client's " + clientNum + " connection");
			while ((clientMsg = reader.readLine()) != null) {
				System.out.println(Thread.currentThread().getName() + " - Message from client "
						+ clientNum + " received: " + clientMsg);
				// Logical processing based on request type
				JSONObject msg = new JSONObject(clientMsg);
				String type = msg.getString(Constants.TYPE);
				JSONObject jsonData = jsonUtil.getData(clientMsg);
				switch (type) {
				case Constants.LOGIN:
					// Set name
					if(loginOperation(jsonData)==true)
						System.out.println(this.clientName+" has joined the lobby.");
					else
						System.out.println(Thread.currentThread().getName()
								+" tried to log in with a duplicate username");
					break;
				case Constants.REFRESH:
					//sent a list of online user
					sendUserlist();
					break;
					
				case Constants.STARTGAME:
					// do something
					break;
				}
			}
			clientSocket.close();
			ClientManager.getInstance().clientDisconnected(this);
			System.out.println(
					Thread.currentThread().getName() + " - Client " + clientNum + " disconnected");
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			// System.out.println( Thread.currentThread().getName() + " - Client " +
			// clientNum + " disconnected");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void write(String msg) {
		try {
			writer.write(msg + "\n");
			writer.flush();
			System.out.println(
					Thread.currentThread().getName() + " - Message sent to client " + clientNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getClientName() {
		return clientName;
	}

	private boolean setClientName(String clientName) {
		// check name, keep unique
		boolean unique = true;
		for(ClientConnection connectedClients :ClientManager.getInstance().getConnectedClients())
		{
			String otherName = connectedClients.getClientName();
			if(clientName.equals(otherName))
				unique = false;
		}
		if(unique==true)
			this.clientName = clientName;
		return unique;
	}


	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getClientCount() {
		return clientCount;
	}

	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}
	
	public boolean loginOperation(JSONObject loginData) throws JSONException, IOException, SocketException {
		JsonUtil jsonUtil = new JsonUtil();
		JSONObject replyPack = new JSONObject();
		JSONObject dataJson = new JSONObject();
		if(this.setClientName(loginData.getString(Constants.USERNAME))==true)
		{
			dataJson.put(Constants.ISUNIQUE, true);
			replyPack = jsonUtil.parse(Constants.LOGIN, dataJson);
			String jsonString = replyPack.toString();
			write(jsonString);
			//broadcast
			return true;
		}
		else
		{
			dataJson.put(Constants.ISUNIQUE, false);
			replyPack = jsonUtil.parse(Constants.LOGIN, dataJson);
			String jsonString = replyPack.toString();
			write(jsonString);
			return false;
		}
	}
	
	public void sendUserlist() throws JSONException, IOException, SocketException {
		//send the list of connected users whose name is not null
		JSONObject [] userList = new JSONObject[ClientManager.getInstance().getConnectedClients().size()];
		JsonUtil jsonUtil = new JsonUtil();
		JSONObject userListPack = new JSONObject();
		JSONObject dataJson = new JSONObject();
		int i =0;
		for(ClientConnection connectedClients :ClientManager.getInstance().getConnectedClients())
		{
			String playerName = connectedClients.getClientName();
			if(playerName!=null)
			{
				boolean status = connectedClients.getStatus();
				JSONObject nameAndStatus = new JSONObject();
				nameAndStatus.put(playerName, status);
				userList[i] = nameAndStatus;
				i++;
			}
		}
		dataJson.put(Constants.USERLIST, userList);
		userListPack = jsonUtil.parse(Constants.REFRESH, dataJson);
		String jsonString = userListPack.toString();
		write(jsonString);
		System.out.println("List sent complete");
	}
}
