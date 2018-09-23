package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

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

	public ClientConnection(Socket clientSocket, int clientNum) {
		try {
			this.clientSocket = clientSocket;
			this.clientNum = clientNum;
			this.reader = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			this.writer = new BufferedWriter(
					new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
		} catch (Exception e) {
			// System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			System.out.println(Thread.currentThread().getName()
					+ " - Reading messages from client's " + clientNum + " connection");
			while ((clientMsg = reader.readLine()) != null) {
				System.out.println(Thread.currentThread().getName() + " - Message from client "
						+ clientNum + " received: " + clientMsg);
				// do something
				//ClientManager.getInstance().processMessage(clientMsg);
			}
			clientSocket.close();
			ClientManager.getInstance().clientDisconnected(this);
			System.out.println(
					Thread.currentThread().getName() + " - Client " + clientNum + " disconnected");
		} catch (SocketException e) {
			// System.out.println( Thread.currentThread().getName() + " - Client " +
			// clientNum + " disconnected");
			e.printStackTrace();
		} catch (IOException e) {
			// System.out.println(e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			// System.out.println(e.toString());
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
			// System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	public String getClientName() {
		return clientName;
	}

	private void setClientName(String clientName) {
		// check name, keep unique
		this.clientName = clientName;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}