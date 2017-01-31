package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	// puerto del servidor
	private final static int PORT = 8889;
	// array que guarda las respuestas de los clientes
	private static Client[] clients = { null, null, null, null };
	private static ArrayList<String[]> questions = new ArrayList<>();

	/*
	 * ejecución del servidor
	 */
	public static void execute() {

		// inserción de preguntas
		String[] question0 = { "¿Cuál fue el ganador de la última RWC?", 
				"1. Australia", "2. Inglaterra",
				"3. Nueva Zelanda", "4. Argentina" };
		String[] question1 = { "¿En que año nació Gabriel García Márquez", 
				"1. 2014", "2. 1982",
				"3. 1920", "4. 1927" };
		questions.add(question0);
		questions.add(question1);

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

	public static boolean setAnswer(int idClient, int answer, int question) {
		boolean add = false;
		
		if(question <= clients[idClient].getAnswers().size()) {
			clients[idClient].getAnswers().add(question, answer);
			add = true;
		}
		
		
		return add;

	}
	
	public static ArrayList<String[]> getQuestions() {
		return questions;
	}

	public static Client[] getClients() {
		return clients;
	}
	
	
}
