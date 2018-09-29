package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import common.Constants;
import common.JsonUtil;
import common.UserInfo;

public class ClientConnection extends Thread {

	private Socket clientSocket;
	private int clientNum;
	private BufferedReader reader;
	private BufferedWriter writer;

	// Received message
	private String clientMsg;
	// Current Count
	private int clientCount;
	//Unique identification containing user name and status
	private UserInfo userInfo;
	

	public ClientConnection(Socket clientSocket, int clientNum) {
		try {
			this.clientSocket = clientSocket;
			this.clientNum = clientNum;
			this.userInfo = new UserInfo("",Constants.INLOBBY);
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
						System.out.println(this.userInfo.getUsername()+" has joined the lobby.");
					else
						System.out.println(Thread.currentThread().getName()
								+" tried to log in with a duplicate username");
					break;
				case Constants.REFRESH:
					//sent a list of online user
					System.out.println(this.userInfo.getUsername()+" request for refresh player list");
					sendUserInfoList();
					break;
				case Constants.INVITE:
					inviteOperation(jsonData);
					break;
				case Constants.INVITEREPLY:
					System.out.println("Invite reply received from "+this.userInfo.getUsername());
					inviteReplyOperation(jsonData);
					break;
				case Constants.STARTGAME:
					// do something
					System.out.println(this.userInfo.getUsername()+" starts the game");
					startGameOperation(clientMsg);
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
			System.out.println( Thread.currentThread().getName() + " - Client " +
			 clientNum + " disconnected");
			ClientManager.getInstance().clientDisconnected(this);
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


	private boolean setClientName(String clientName) {
		// check name, keep unique
		boolean unique = true;
		for(ClientConnection connectedClients :ClientManager.getInstance().getConnectedClients())
		{
			String otherName = connectedClients.getUserInfo().getUsername();
			if(clientName.equals(otherName))
				unique = false;
		}
		if(unique==true)
			this.userInfo.setUsername(clientName);
		return unique;
	}


	public int clientCount() {
		return clientCount;
	}

	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}
	
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	//check if the username is duplicate. If not, approve log in, reset the username, and broadcast the new player's join
	public boolean loginOperation(JSONObject loginData) throws JSONException, IOException, SocketException {
		JsonUtil jsonUtil = new JsonUtil();
		JSONObject replyPack = new JSONObject();
		JSONObject dataJson = new JSONObject();
		if(this.setClientName(loginData.getString(Constants.USERNAME))==true)
		{
			this.userInfo = new UserInfo(loginData.getString(Constants.USERNAME),Constants.INLOBBY,true);
			dataJson.put(Constants.ISUNIQUE, true);
			replyPack = jsonUtil.parse(Constants.LOGIN, dataJson);
			String jsonString = replyPack.toString();
			write(jsonString);
			//broadcast the change in user info list
			ClientManager.getInstance().clientConnected(this);
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
	
	public boolean inviteOperation(JSONObject inviteData) throws JSONException, IOException, SocketException {
		JsonUtil jsonUtil = new JsonUtil();
		JSONObject invitationPack = new JSONObject();
		JSONObject dataJson = new JSONObject();
		//form a json pack containing source client name
		dataJson.put(Constants.USERNAME, this.getUserInfo().getUsername());
		invitationPack = jsonUtil.parse(Constants.INVITE,dataJson);
		String jsonString = invitationPack.toString();
		
		//send the pack to appointed client
		String destinationName = (String)inviteData.get(Constants.USERNAME);
		
		for(ClientConnection clientConnection: ClientManager.getInstance().getConnectedClients()) {
			if(clientConnection.getUserInfo().getUsername().equals(destinationName))
			{
				clientConnection.write(jsonString);
				System.out.println("Invitation sent to "+clientConnection.getUserInfo().getUsername());
				return true;
			}
		}
		System.out.println("Did not find the player to invite");
		return false;
		
	}
	
	public boolean inviteReplyOperation(JSONObject inviteReplyData) throws JSONException, IOException, SocketException {
		JsonUtil jsonUtil = new JsonUtil();
		boolean invitationAccepted = (boolean)inviteReplyData.get(Constants.ISACCEPTED);
		boolean inviterInRoom = false;
		String inviterName = inviteReplyData.getString(Constants.USERNAME);
		if(invitationAccepted==true)
		{
			//add the invited to the room
			ClientManager.getInstance().getInRoomClients().add(this);
			System.out.println("Add "+this.getUserInfo().getUsername()+" to the room");
			UserInfo.updataUserStatusInList(this.userInfo.getUsername(), Constants.INROOM);

		 	System.out.println(UserInfo.getUserInfoList().get(0).getStatus());
			
			//check if the inviter is already in the room
			for(ClientConnection inRoomClients : ClientManager.getInstance().getInRoomClients())
			{
				if(inRoomClients.getUserInfo().getUsername().equals(inviterName))
				{
					inviterInRoom=true;
					break;
				}
			}
		}
		
		JSONObject dataJson = new JSONObject();
		dataJson.put(Constants.USERNAME, this.userInfo.getUsername());
		dataJson.put(Constants.ISACCEPTED, invitationAccepted);
		JSONObject inviteReplyPack = jsonUtil.parse(Constants.INVITEREPLY, dataJson);
		String inviteReplyString = inviteReplyPack.toString();
		//transfer the inviteReplyString to the inviter
		for(ClientConnection clientConnection : ClientManager.getInstance().getConnectedClients())
		{
			if(clientConnection.getUserInfo().getUsername().equals(inviterName))
			{
				clientConnection.write(inviteReplyString);
				//add inviter to the room if following conditions are met
				if((inviterInRoom==false) && (invitationAccepted==true))
				{
					ClientManager.getInstance().getInRoomClients().add(clientConnection);
					System.out.println("Add "+clientConnection.getUserInfo().getUsername()+" to the room");
					UserInfo.updataUserStatusInList(clientConnection.getUserInfo().getUsername(), Constants.INROOM);
				}
			}
		}
		
		//broadcast the change in status
		String jsonString = UserInfo.getUserInfoListJsonString();
		ClientManager.getInstance().broadcast(jsonString);
		System.out.println("Broadcast complete");
		if(invitationAccepted==true)
			return true;
		else 
			return false;
	}
	
	public void startGameOperation(String jsonString) throws JSONException, IOException, SocketException {
		//broadcast start game command to all in room players
		ClientManager.getInstance().broadcast(jsonString,ClientManager.getInstance().getInRoomClients());
		ClientManager.getInstance().getInRoomClients().clear();
		//change the status of these players to "in game" and broadcast the change to all players
		ArrayList<UserInfo> inRoomList = UserInfo.subListWithSpecificStatus(Constants.INROOM);
		UserInfo.updataUserStatusInList(inRoomList, Constants.INGAME);
		String refreshPack = UserInfo.getUserInfoListJsonString();
		ClientManager.getInstance().broadcast(refreshPack);
	}

	public void sendUserInfoList() throws JSONException, IOException, SocketException {
		//send the list of connected users whose name is not null
		String jsonString = UserInfo.getUserInfoListJsonString();
		write(jsonString);
		System.out.println("List sent complete");
	}
}