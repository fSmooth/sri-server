package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Service extends Thread{
	// atributos
	private Socket clientSocket;
	private Client client;
	private Scanner in;
	private PrintWriter out;
	
	/**
	 * Constructor
	 * @param s : socket para la comunicación
	 * @param c : cliente que usará el servicio
	 */
	public Service(Socket clientSocket, Client client) {
		this.clientSocket = clientSocket;
		this.client = client;
	}
	
	
	@Override
	public void run() {
		try {
			// flujos de entrada y salida del servidor.
			in = new Scanner(clientSocket.getInputStream());
			out = new PrintWriter(clientSocket.getOutputStream());
			
			// almacena el comando introducid
			String command = in.nextLine();
			
			while(!command.equals("EXIT")) {
				switch (command) {
				case "ID": // devuelve el ID del cliente
					out.print("Su ID es: ");
					out.println(client.getId());
					out.flush();
					break;

				default:
					out.println("Unknown command.");
					out.flush();
					break;
				}
				
				command = in.nextLine();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
