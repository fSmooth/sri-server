package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	// puerto del servidor
	private final static int PORT = 8889;
	// array que guarda las respuestas de los clientes
	private static Client[] clients = {null, null, null, null};
	
	/*
	 *  ejecución del servidor
	 */
	public static void execute() {

		// Socket para la comunicación del servidor
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("waiting for connections...");
			
			while(true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("client connected.");
				
				//variable que guarda
				//el id del cliente conectado
				int client = -1;
				
				int counter = 0;
				while(client == -1) {
					if(clients[counter] == null) {
						client = counter;
						clients[counter] = new Client(counter);
					}
				}
				
				
				
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
