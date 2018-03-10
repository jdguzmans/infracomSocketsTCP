package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;

public class Worker extends Thread {
	
	private int id;
	private Socket clientSocket;
	
	public Worker(int i) {
		this.id = i;
	}
	
	public void receiveClientSocker(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	public boolean isNotRunning () {
		return clientSocket == null || clientSocket.isClosed();
	}

	public void run() {
		log("Connection");
		String clientSentence = "";
		
		try {	
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());			
			
			clientSentence = inFromClient.readLine();
			log("IN: " + clientSentence);
			
			outToClient.writeBytes("H\n");
			log("RESPONDIO");
			
			outToClient.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	private void log(String wat) {
		System.out.println(id + ": " + wat);
	}
}
