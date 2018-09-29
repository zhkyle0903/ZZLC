package client.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import common.Constants;
import common.UserInfo;

public class testTable {

	private JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testTable window = new testTable();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public testTable() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		table = new JTable();
		UserInfo test = new UserInfo("abc",Constants.INGAME,true);
		UserInfo test2 = new UserInfo("abccc",Constants.INGAME,true);
		
		String [][] tableData = UserInfo.listToTableData();
		String [] columnNames = {"User Name", "Status"};
		table = new JTable(tableData, columnNames);
		table.setBounds(89, 66, 161, 103);
        JScrollPane scroll = new JScrollPane(table);  
        scroll.setSize(300, 200);  
        frame.add(scroll);  
		
		
	}
}
