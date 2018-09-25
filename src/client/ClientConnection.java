package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

import common.JsonUtil;
import common.Constants;

public class ClientConnection {

	private static ClientConnection instance;
	private Socket client;
	private BufferedReader reader;
	private BufferedWriter writer;
	private String serverMsg;
	private JSONObject[] userlist;
	
	private ClientConnection() {
	}

	public static ClientConnection getInstance() {
		if (instance == null) {
			instance = new ClientConnection();
		}
		return instance;
	}

	public void clientConnected(Socket client) {
		try {
			this.client = client;
			this.reader = new BufferedReader(
					new InputStreamReader(client.getInputStream(), "UTF-8"));
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
	
	public boolean tryToLogin(String username) throws JSONException, IOException, SocketException{
		JsonUtil jsonUtil = new JsonUtil();
		JSONObject dataJson = new JSONObject();
		dataJson.put(Constants.USERNAME, username);
		JSONObject loginPack = jsonUtil.parse(Constants.LOGIN, dataJson);
		String jsonString = loginPack.toString();
		if(sendMsg(jsonString)==true){
			String type = jsonUtil.getType(serverMsg=reader.readLine());
			if(type.equals(Constants.LOGIN)){
				if((boolean)jsonUtil.getData(serverMsg).get(Constants.ISUNIQUE)==true)
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	public boolean refreshUserList() throws JSONException, IOException, SocketException{
		JsonUtil jsonUtil = new JsonUtil();
		JSONObject dataJson = new JSONObject();
		JSONObject refreshPack = jsonUtil.parse(Constants.REFRESH, dataJson);
		String jsonString = refreshPack.toString();
		if(sendMsg(jsonString)==true) {
			if(jsonUtil.getType(serverMsg=reader.readLine()).equals(Constants.REFRESH)){
				userlist = (JSONObject[]) jsonUtil.getData(serverMsg).get(Constants.USERLIST);
				return true;
			}
			else 
				return false;
		}
		else
			return false;
		
	}

	public Socket getClient() {
		return client;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public BufferedWriter getWriter() {
		return writer;
	}
}