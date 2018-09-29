package client.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.json.JSONObject;

import client.ClientConnection;
import common.Constants;
import common.JsonUtil;

public class InvitationFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	private String inviteMsg;
	private static String inviter;
	
	public InvitationFrame(String inviteMsg,String inviter)
	{
		this.inviteMsg = inviteMsg;
		InvitationFrame.inviter = inviter;
		initialize();
	}
	
	private void initialize()
	{
		this.setTitle("Invitation");
		this.setSize(inviteMsg.length()*7+40, 150);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) ((screenSize.getWidth() - this.getWidth()) / 2),
				(int) ((screenSize.getHeight() - this.getHeight()) / 2));
		
		Point center = new Point(this.getWidth()/2, this.getHeight()/2);
		
		JLabel inviteLabel = new JLabel(inviteMsg);
		inviteLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		inviteLabel.setSize(inviteMsg.length()*7, 21);
		inviteLabel.setLocation(10+center.x-inviteLabel.getWidth()/2,center.y-40);
		
		JButton btnOK = new JButton("OK");
		JButton btnDecline = new JButton("Decline");
		Insets margin = new Insets(0,0,0,0);
		btnOK.setMargin(margin);
		btnOK.setSize(50, 23);
		btnDecline.setMargin(margin);
		btnDecline.setSize(50, 23);
		btnOK.setLocation(center.x-70, center.y+5);
		btnDecline.setLocation(center.x+20, center.y+5);

		this.getContentPane().add(inviteLabel); 
		this.getContentPane().add(btnOK);
		this.getContentPane().add(btnDecline);
		
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if(ClientConnection.getInstance().sendMsg(InvitationFrame.createAcceptJsonString())==true)
					System.out.println("Acceptance sent");
				dispose();
			};
		});
		
		btnDecline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if(ClientConnection.getInstance().sendMsg(InvitationFrame.createDeclineJsonString())==true)
					System.out.println("Refusal sent");
				dispose();
			};
		});
	}
	
	public static String createAcceptJsonString() {
		JsonUtil jsonUtil = new JsonUtil();
		JSONObject replyPack = new JSONObject();
		JSONObject dataJson = new JSONObject();
		
		dataJson.put(Constants.ISACCEPTED, true);
		dataJson.put(Constants.USERNAME, InvitationFrame.inviter);
		replyPack = jsonUtil.parse(Constants.INVITEREPLY, dataJson);
		return replyPack.toString();
	}
	
	public static String createDeclineJsonString() {
		JsonUtil jsonUtil = new JsonUtil();
		JSONObject replyPack = new JSONObject();
		JSONObject dataJson = new JSONObject();
		
		dataJson.put(Constants.ISACCEPTED, false);
		dataJson.put(Constants.USERNAME, InvitationFrame.inviter);
		replyPack = jsonUtil.parse(Constants.INVITEREPLY, dataJson);
		return replyPack.toString();
	}
}
