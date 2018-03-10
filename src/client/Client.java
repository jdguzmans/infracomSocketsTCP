package client;
import java.io.*;
import java.net.*;

import server.Server;

public class Client {
	private final static String SERVER_ADDRESS = "157.253.214.205";
	
	public static void main(String argv[]) throws Exception {
		String console;
		String fromServer;
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket(SERVER_ADDRESS, Server.SERVER_PORT);
		
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		outToServer.writeBytes("H" + '\n');
		System.out.println("YA MANDO HOLA");
	
		fromServer = inFromServer.readLine();
		System.out.println("FROM SERVER: " + fromServer);
		
		clientSocket.close();
	}
}
