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

@SuppressWarnings("serial")
public class ErrorFrame extends JFrame{
	private String ErrorMsg;
	
	public ErrorFrame(String ErrorMsg)
	{
		this.ErrorMsg = ErrorMsg;
		initialize();
	}
	
	private void initialize()
	{
		this.setTitle("Error");
		this.setSize(ErrorMsg.length()*7+20, 150);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) ((screenSize.getWidth() - this.getWidth()) / 2),
				(int) ((screenSize.getHeight() - this.getHeight()) / 2));
		
		Point center = new Point(this.getWidth()/2, this.getHeight()/2);
		
		JLabel errorLabel = new JLabel(ErrorMsg);
		errorLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		errorLabel.setSize(ErrorMsg.length()*7, 21);
		errorLabel.setLocation(10+center.x-errorLabel.getWidth()/2,center.y-40);
		
		JButton btnOK = new JButton("OK");
		Insets margin = new Insets(0,0,0,0);
		btnOK.setMargin(margin);
		btnOK.setSize(50, 23);
		btnOK.setLocation(center.x-25, center.y+5);

		this.getContentPane().add(errorLabel); 
		this.getContentPane().add(btnOK);
		
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				dispose();
			};
		});
	}
}
