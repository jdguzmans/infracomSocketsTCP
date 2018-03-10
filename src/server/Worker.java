package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;

public class Worker extends Thread {
	
	private Socket clientSocket;
	
	public Worker(Socket socket) {
		clientSocket = socket;
	}

	public void run() {
		System.out.println("Worker tiene conexión");
		String clientSentence = "";
		try {
			
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());			
			
			clientSentence = inFromClient.readLine();
			System.out.println("DEL CLIENTE: " + clientSentence);
			
			if (clientSentence.equals("H")) outToClient.writeBytes("H" + '\n');
			else outToClient.writeBytes("ERROR" + '\n');
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
}
