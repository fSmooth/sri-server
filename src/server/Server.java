package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	// puerto del servidor
	private final static int PORT = 8889;
	// array que guarda las respuestas de los clientes
	private static Client[] clients = { null, null, null, null };

	/*
	 * ejecución del servidor
	 */
	public static void execute() {

		// Socket para la comunicación del servidor
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("waiting for connections...");

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("client connected.");

				/**
				 * variable que controla si hay una posición libre
				 */
				boolean isEmpty = false;

				int counter = 0;
				while (!isEmpty && counter < clients.length) {
					if (clients[counter] == null) {
						isEmpty = true;
						clients[counter] = new Client(counter);
					} else
						counter++;
				}

				if (isEmpty) {
					Service service = new Service(clientSocket, clients[counter].getId());
					service.start();
				}

				else {
					System.err.println("exceeded client limit.");
					clientSocket.close();
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static boolean setAnswer(int idClient, int answer) {
		boolean add = false;
		if (clients[idClient].getAnswers().add(answer))
			add = true;

		return add;

	}
}
