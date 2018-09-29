package client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

import client.gui.ErrorFrame;
import common.JsonUtil;
import common.Constants;
import common.UserInfo;

public class ClientConnection {

	private static ClientConnection instance;
	private Socket client;
	private BufferedWriter writer;
	private String username;
	
	private ClientConnection() {
	}

	public static ClientConnection getInstance() {
		if (instance == null) {
			instance = new ClientConnection();
		}
		return instance;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void clientConnected(Socket client) {
		try {
			this.client = client;
			this.writer = new BufferedWriter(
					new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean sendMsg(String msg) {
		if (writer != null && client.isConnected()) {
			try {
				// send msg to server
				writer.write(msg + "\n");
				writer.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	//returns true if login success, otherwise false
	public boolean tryToLogin(String username) throws JSONException, IOException, SocketException{
		JsonUtil jsonUtil = new JsonUtil();
		JSONObject dataJson = new JSONObject();
		dataJson.put(Constants.USERNAME, username);
		JSONObject loginPack = jsonUtil.parse(Constants.LOGIN, dataJson);
		String jsonString = loginPack.toString();
		if(sendMsg(jsonString)==true)
			return true;
		else
			return false;
	}

	//invite a player, returns true if successfully sent
	public boolean invite(String username) throws JSONException, IOException, SocketException {
		//check if the player is available to invite
		String status = UserInfo.checkUserStatusInList(username);
		if(status==null)
		{
			ErrorFrame error = new ErrorFrame("This user does not exist");
			error.setVisible(true);
			return false;
		}
		
		//player in game
		else if(status.equals(Constants.INGAME))
		{
			ErrorFrame error = new ErrorFrame("This player is already in a game");
			error.setVisible(true);
			return false;
		}
		
		//player in room
		else if(status.equals(Constants.INROOM))
		{
			ErrorFrame error = new ErrorFrame("This player is already in the preparing room");
			error.setVisible(true);
			return false;
		}
		
		//player in lobby(available)
		{
			JsonUtil jsonUtil = new JsonUtil();
			JSONObject dataJson = new JSONObject();
			dataJson.put(Constants.USERNAME, username);
			JSONObject invitePack = jsonUtil.parse(Constants.INVITE, dataJson);
			String jsonString = invitePack.toString();
			if(sendMsg(jsonString)==true){
				return true;
			}
			else return false;
		}
	}
	
	public boolean startGame() throws JSONException, IOException, SocketException {
		if(UserInfo.checkUserStatusInList(this.username).equals(Constants.INROOM))
		{
			JsonUtil jsonUtil = new JsonUtil();
			JSONObject dataJson = new JSONObject();
			dataJson.put(Constants.USERNAME, this.username);
			JSONObject startGamePack = jsonUtil.parse(Constants.STARTGAME, dataJson);
			String jsonString = startGamePack.toString();
			if(sendMsg(jsonString)==true) 
				return true;
			else
				return false;
			}
		else
		{
			ErrorFrame error = new ErrorFrame("You must be in lobby room before starting the game");
			error.setVisible(true);
			return false;
		}
	}

	//send a refresh request to the server
	public boolean refreshUserlist() throws JSONException, IOException, SocketException{
		JsonUtil jsonUtil = new JsonUtil();
		JSONObject dataJson = new JSONObject();
		JSONObject refreshPack = jsonUtil.parse(Constants.REFRESH, dataJson);
		String jsonString = refreshPack.toString();
		if(sendMsg(jsonString)==true) 
				return true;
		else
			return false;
		
	}

	public Socket getClient() {
		return client;
	}

	public BufferedWriter getWriter() {
		return writer;
	}
	
}