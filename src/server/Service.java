package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class Service extends Thread {
	// atributos
	private Socket clientSocket;
	private int idClient;
	private Scanner in;
	private PrintWriter out;

	/**
	 * Constructor
	 * 
	 * @param s
	 *            : socket para la comunicación
	 * @param c
	 *            : cliente que usará el servicio
	 */
	public Service(Socket clientSocket, int idClient) {
		this.clientSocket = clientSocket;
		this.idClient = idClient;
	}

	@Override
	public void run() {
		try {
			// flujos de entrada y salida del servidor.
			in = new Scanner(clientSocket.getInputStream());
			out = new PrintWriter(clientSocket.getOutputStream());

			// almacena el comando introducid
			String command = in.nextLine();

			while (!command.equals("EXIT")) {
				switch (command) {
				case "ID": // devuelve el ID del cliente
					out.print("Su ID es: ");
					out.println(idClient);
					break;
				case "QUESTION": // devuelve la pregunta con sus posibles
									// respuestas
					out.println("¿Cuál fue el ganador de la última RWC?");
					out.println("======================================");
					out.println("1. Australia");
					out.println("2. Inglaterra");
					out.println("3. Nueva Zelanda");
					out.println("4. Argentina");
					break;

				default:
					// respuesta a la pregunta
					String[] splitCommand = command.split(" ");

					if (splitCommand[0].equals("ANSWER") && StringUtils.isNumeric(splitCommand[1])) {
						if (Server.setAnswer(idClient, Integer.valueOf(splitCommand[1])))
							out.println("added answer");
						else
							out.println("answer not added");

					} else
						out.println("Unknown command.");
					break;
				}

				out.flush();
				command = in.nextLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
