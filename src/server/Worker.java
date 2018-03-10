package server;

import java.io.*;
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
		try {	
			log("CONNECTION");
			String clientSentence = "";
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());			

			boolean done = false;
			while (!done) {				
				clientSentence = inFromClient.readLine();
				if (clientSentence == null) done = true;
				else {
					String[] splitBySpace = clientSentence.split(" ");
					String command = splitBySpace[0];
					String param = "";
					if (splitBySpace.length != 1) param = splitBySpace[1];
					log("IN: " + command + " " + param);


					switch(command) {
					case "H":
						outToClient.writeBytes("H\n");
						break;
					case "X":{
						done = true;
						outToClient.writeBytes("X\n");
						outToClient.close();
						break;
					}
					case "LI": {
						File dir = new File(Server.FILE_DIR);
						outToClient.writeBytes("LI\n");
						File[] fileNames = dir.listFiles();
						for (File fileName : fileNames) {
							outToClient.writeBytes(fileName.getName() + " " + fileName.length() + "\n");
						}
						outToClient.writeBytes("LI\n");
						break;
					}
					case "F": {
						File searched = new File(Server.FILE_DIR + param);
						if (!searched.exists()) {
							outToClient.writeBytes("E FileDoesNotExist\n");
						} 
						else {
							outToClient.writeBytes("F\n");
							
							FileInputStream fis = new FileInputStream(searched);

							int count;
							byte[] buffer = new byte[512];
							
							while ((count=fis.read(buffer)) > 0) {
								outToClient.write(buffer, 0, count);
							}
							
							log("Termino");
							fis.close();
							outToClient.writeBytes("F\n");
						}
						break;
					}
					default: {
						outToClient.writeBytes("E CommandNotRecognized\n");
					}
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

	private void log(String wat) {
		System.out.println(id + ": " + wat);
	}
}
