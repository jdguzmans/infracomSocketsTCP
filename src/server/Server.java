package server;
import java.io.*;
import java.net.*;

public class Server {

	public final static int SERVER_PORT = 6789;
	private static Worker[] workers;
	private final static int WORKERS = 2;
	public final static String FILE_DIR = "./files/";

	public static void main(String argv[]) throws Exception {
		System.out.println("SERVER STARTED");
		workers = new Worker[WORKERS];
		@SuppressWarnings("resource")
		ServerSocket welcomeSocket = new ServerSocket(SERVER_PORT);

		for (int i = 0; i < workers.length; i++) {
			if (workers[i] == null) {
				workers[i] = new Worker(i);
			}
		}
		
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			Worker worker = null;
			
			for (int i = 0; i < workers.length && worker == null; i++) {
				if (workers[i].isNotRunning()) {
					workers[i].receiveClientSocker(connectionSocket);
					worker = workers[i];
				}
			}
			
			if (worker == null) {	
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());			
				
				inFromClient.readLine();
				outToClient.writeBytes("U" + '\n');
				
				outToClient.close();
			}
			else {
				worker.start();
			}
		}
	}
}
