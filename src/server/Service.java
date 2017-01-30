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
	private int questionCounter;
	private boolean isFinished;

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
		this.questionCounter = 0;
		this.isFinished = false;
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
					if (questionCounter < Server.getQuestions().size()) {
						out.println(Server.getQuestions().get(questionCounter)[0]);
						out.println("=============================");
						out.println(Server.getQuestions().get(questionCounter)[1]);
						out.println(Server.getQuestions().get(questionCounter)[2]);
						out.println(Server.getQuestions().get(questionCounter)[3]);
						out.println(Server.getQuestions().get(questionCounter)[4]);

					} else {
						out.println("no questions left.");
						isFinished = true;
					}

					break;
				case "RESTART": // vuelve a comenzar las preguntas
					questionCounter = 0;
					isFinished = false;
					break;

				default:
					// respuesta a la pregunta
					String[] splitCommand = command.split(" ");

					if (splitCommand[0].equals("ANSWER") && StringUtils.isNumeric(splitCommand[1])) {
						if (isFinished) // no se pueden responder a más
										// preguntas
							out.println("finished.");
						else if (Server.setAnswer(idClient, Integer.valueOf(splitCommand[1]), questionCounter)) {
							questionCounter++;
							out.println("added answer");
						}

						else
							out.println("answer not added: You must respond in order");

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
