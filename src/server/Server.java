package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	// puerto del servidor
	private final static int PORT = 8889;
	
	// ejecución del servidor
	public static void execute() {

		// Socket para la comunicación del servidor
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("waiting for connections...");
			
			while(true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("client connected.");
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
