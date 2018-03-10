package client;
import java.io.*;
import java.net.*;

import server.Server;

public class Client {
	private final static String SERVER_ADDRESS = "www.coralingenieros.com";

	private static void saveFile(Socket clientSock, String nameFile, String size) throws IOException {
		
		DataInputStream dis = new DataInputStream(clientSock.getInputStream());
		FileOutputStream fos = new FileOutputStream( new File("./down/"+nameFile));
		System.out.println("START SAVE FILE");

		byte[] buffer = new byte[512];

		int filesize = Integer.parseInt(size); // Send file size in separate msg
		int read = 0;
		int totalRead = 0;
		int remaining = filesize;
		while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
			remaining -= read;
			System.out.println("read " + totalRead + " bytes.");
			fos.write(buffer, 0, read);
		}
		
		System.out.println("DONE ");

		fos.close();
		dis.close();
	}
//3 134 644
	
// 3 136 091
//314 57 28
//3145728
	public static void main(String argv[]) throws Exception {
		String console;
		String fromServer;
		boolean termino =false;

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket(SERVER_ADDRESS, Server.SERVER_PORT);

		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		outToServer.writeBytes("H" + '\n');
		System.out.println("TO SERVER HOLA");

		fromServer = inFromServer.readLine();
		System.out.println("FROM SERVER: " + fromServer);


		while(!termino){
			boolean ternimo =false;
			String command = inFromUser.readLine();	
			
			String[] splitC = command.split(" ");
			String commandC = splitC[0];
			String param1C = " ";
			String param2C = " ";
			
			if(splitC.length > 1){
				param1C=splitC[1];
				if(splitC.length > 2){
					param2C=splitC[2];
				}
			}
			
			
			outToServer.writeBytes(command + '\n');
			System.out.println("TO SERVER "+ command);

			fromServer = inFromServer.readLine();

			String[] split = fromServer.split(" ");
			String comman = split[0];
			String param1 = " ";
			String param2 = " ";
			
			if(split.length > 1){
				param1=split[1];
				if(split.length > 2){
					param2=split[2];
				}
			}
			System.out.println("params" + param1 +" 2 " +param2);


			switch(comman) {
			case "LI":
				ternimo =false;
				//espero hasta que llegan todos los nombres de archivos
				while(!ternimo){
					fromServer = inFromServer.readLine();
					if(fromServer.equals("LI")) ternimo=true;
					System.out.println("FROM SERVER: " + fromServer);
				}
				System.out.println("TERMINO LI");
				break;
			case "X":
				clientSocket.close();
				System.out.println("TERMINADO");
				break;

			case "F":
				ternimo =false;

				saveFile(clientSocket, param1C, param2C);

				System.out.println("TERMINO F");
				//inFromServer.readLine();
				break;

			case "E":
				System.out.println("FROM SERVER ERROR: " + param1);
				break;
			}
		}
		clientSocket.close();
	}



}
