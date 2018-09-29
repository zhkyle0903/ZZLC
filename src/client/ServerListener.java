package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

import client.gui.ErrorFrame;
import client.gui.InvitationFrame;
import client.gui.MainFrame;
import common.Constants;
import common.JsonUtil;
import common.UserInfo;

public class ServerListener extends Thread{
	private String serverMsg;
	private Socket serverSocket;
	private BufferedReader reader;
	
	public ServerListener(Socket serverSocket)
	{
		try {
			this.serverSocket = serverSocket;
			this.reader = new BufferedReader(
					new InputStreamReader(serverSocket.getInputStream(), "UTF-8"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		JsonUtil jsonUtil = new JsonUtil();
		try {
			while((serverMsg = reader.readLine())!=null) {
				JSONObject msg = new JSONObject(serverMsg);
				String type = msg.getString(Constants.TYPE);
				switch (type) {
				case Constants.LOGIN:
					if((boolean)jsonUtil.getData(serverMsg).get(Constants.ISUNIQUE)==true)
					{
						System.out.println("Log in successful");
						MainFrame.getInstance().login();
					}
					else
					{
						ErrorFrame errorWindow = new ErrorFrame("Login Failed: Username is already being used.");
						errorWindow.setVisible(true);
					}
					break;
				case Constants.ALLUSERINFO:
					System.out.println("Lastest player list received.");
					UserInfo.updateAllUserInfoFromJsonString(serverMsg);
					MainFrame.getInstance().getLobbyPanel().refreshLobby();
					break;
				case Constants.INVITE:
					String inviter = (String)jsonUtil.getData(serverMsg).get(Constants.USERNAME);
					InvitationFrame inviteWindow = new InvitationFrame("Player "+inviter+" invites you for a game",inviter);
					inviteWindow.setVisible(true);	
					break;
				case Constants.INVITEREPLY:
					if((boolean)jsonUtil.getData(serverMsg).get(Constants.ISACCEPTED))
					{
						ErrorFrame accepted = new ErrorFrame((String)jsonUtil.getData(serverMsg).get(Constants.USERNAME)+" has accepted your invitation");
						accepted.setVisible(true);
					}
					else
					{
						ErrorFrame accepted = new ErrorFrame((String)jsonUtil.getData(serverMsg).get(Constants.USERNAME)+" has declined your invitation");
						accepted.setVisible(true);
					}
					break;	
				case Constants.STARTGAME:
					String starter = (String)jsonUtil.getData(serverMsg).get(Constants.USERNAME);
					System.out.println(starter+" starts the game");
					MainFrame.getInstance().startGame();
				}
			}
			serverSocket.close();
			System.out.println("Disconnect with server");		
		}
		catch (JSONException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
