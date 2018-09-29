package client.gui;

import common.Constants;
import common.UserInfo;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.*;

import org.json.JSONException;

import client.ClientConnection;


@SuppressWarnings("serial")
public class LobbyPanel extends JPanel {
	
	
	private String [][] tableData;
	private final String [] columnNames = {"User Name", "Status"};
	private JTable lobbyTable;
	
	private JList<String> lobbyRoom;
	private String[] listData;
	
    private JScrollPane tableScroll;
    private JScrollPane listScroll;

	public LobbyPanel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		setPreferredSize(new Dimension(800, 640));

		// Create component
		JButton startButton = new JButton("Start Game");
		JButton inviteButton = new JButton("Invite");
		JButton refreshButton = new JButton("Refresh");
		JLabel yourName = new JLabel(ClientConnection.getInstance().getUsername());
		
		//Adjust Components Attributes
		startButton.setPreferredSize(new Dimension(100, 24));
		inviteButton.setPreferredSize(new Dimension(100,24));
		refreshButton.setPreferredSize(new Dimension(100,24));
		yourName.setPreferredSize(new Dimension(200,24));
		
		// Add component
		add(startButton);
        //add(playerList);
        add(inviteButton);
        //add refresh button
        add(refreshButton);
        //add username label
        add(yourName);
            
		// Add listener
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// do something
				try {
					if(ClientConnection.getInstance().startGame()==true)
						System.out.println("Game Start");
					else
						System.out.println("Failed to start a game");
				} catch (JSONException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		inviteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// invite action here
				int selectedRow = lobbyTable.getSelectedRow();
				if(selectedRow<0)
				{
					ErrorFrame errorWindow = new ErrorFrame("Please select one player to invite");
					errorWindow.setVisible(true);
				}
				try {
					ClientConnection.getInstance().invite(UserInfo.getUserInfoList().get(selectedRow).getUsername());
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(ClientConnection.getInstance().refreshUserlist())
						System.out.println("Refresh request sent");
					else
						System.out.println("Fail to send refresh request");
				} catch (JSONException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	//refresh the lobby
	public void refreshLobby() throws InterruptedException {
		//generate lobby table
		tableData = UserInfo.listToTableData();
		if(tableScroll!=null)
			this.remove(tableScroll);
		lobbyTable = new JTable(tableData,columnNames);
		lobbyTable.setPreferredSize(new Dimension(300,200));
		tableScroll = new JScrollPane(lobbyTable);  
        tableScroll.setPreferredSize(new Dimension(300,200)); 
		//set position
		this.add(tableScroll);
		
		//generate lobby room
		ArrayList<UserInfo> inRoomList = UserInfo.subListWithSpecificStatus(Constants.INROOM);
		listData = new String[inRoomList.size()];
		int i = 0;
		for(UserInfo userinfo : inRoomList)
		{
			listData[i] = userinfo.getUsername();
			i++;
		}
		if(listScroll!=null)
			this.remove(listScroll);
		lobbyRoom = new JList<String>(listData);
		lobbyRoom.setPreferredSize(new Dimension(300,200));
		listScroll = new JScrollPane(lobbyRoom);  
        listScroll.setPreferredSize(new Dimension(300,200)); 
		this.add(listScroll);
		
		this.revalidate();
		this.repaint();
		
	}

}