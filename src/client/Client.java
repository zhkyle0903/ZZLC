package client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import client.config.Config;
import client.config.ConfigLoader;
import client.gui.ErrorFrame;
import client.gui.MainFrame;

public class Client {

	private static int port;
	private static String ip;

	public static void main(String[] args) {
		Socket client;
		if (args.length == 0) {
			Config config = ConfigLoader.load();
			ip = config.getServerAddress();
			port = config.getServerPort();
		} else if (args.length == 2) {
			ip = args[0];
			try {
				port = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.out.println("Unknown port: " + args[1]);
				System.exit(0);
			}
		} else {
			String errorMsg = "Unknown parameters";
			System.out.println("errorMsg");
			ErrorFrame error = new ErrorFrame(errorMsg);
			error.setVisible(true);
		}
		try {
			// Establish the connection
			client = new Socket();
			client.connect(new InetSocketAddress(ip, port), 10000);
			ClientConnection.getInstance().clientConnected(client);
			ServerListener serverListener = new ServerListener(client);
			serverListener.start();
			MainFrame.getInstance().setVisible(true);
		} catch (UnknownHostException e) {
			String errorMsg = "Unknown host: " + ip;
			System.out.println(errorMsg);
			ErrorFrame error = new ErrorFrame(errorMsg);
			error.setVisible(true);
		} catch (ConnectException e) {
			String errorMsg = "Connection refused from " + ip + ":" + port;
			System.out.println(errorMsg);
			ErrorFrame error = new ErrorFrame(errorMsg);
			error.setVisible(true);
		} catch (SocketTimeoutException e) {
			String errorMsg = "Connect timed out";
			System.out.println(errorMsg);
			ErrorFrame error = new ErrorFrame(errorMsg);
			error.setVisible(true);
		} catch (IOException e) {
			String errorMsg = e.toString();
			System.out.println(errorMsg);
			ErrorFrame error = new ErrorFrame(errorMsg);
			error.setVisible(true);
		}
	}
}