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
					if (!isFinished) {
						out.println(Server.getQuestions().get(questionCounter)[0]);
						out.println("=============================");
						out.println(Server.getQuestions().get(questionCounter)[1]);
						out.println(Server.getQuestions().get(questionCounter)[2]);
						out.println(Server.getQuestions().get(questionCounter)[3]);
						out.println(Server.getQuestions().get(questionCounter)[4]);

					} else {
						out.println("no questions left.");
					}

					break;
				case "RESTART": // vuelve a comenzar las preguntas
					questionCounter = 0;
					isFinished = false;
					break;

				case "STATISTICS": // muestra las respuestas de los clientes
					statistics(out);
					break;

				default:
					// respuesta a la pregunta
					String[] splitCommand = command.split(" ");

					if (splitCommand[0].equals("ANSWER") && StringUtils.isNumeric(splitCommand[1])) {
						if (isFinished) // no se puede responder a más
										// preguntas
							out.println("finished.");
						else if (Server.setAnswer(idClient, Integer.valueOf(splitCommand[1]), questionCounter)) {
							questionCounter++;
							out.println("added answer");
							
							if(questionCounter == Server.getQuestions().size())
								isFinished = true;
						}

						else
							out.println("answer not added: You must respond in order");

					// comando desconocido
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

	/**
	 * Método que muestra las respuestas seleccionadas por los clientes a cada
	 * pregunta.
	 * 
	 * @param out
	 *            : flujo donde se imprimirán los resultados
	 */
	private static void statistics(PrintWriter out) {
		for (int i = 0; i < Server.getQuestions().size(); i++) {
			int first = 0;
			int second = 0;
			int third = 0;
			int forth = 0;
			
			out.println("Question " + i +": " + Server.getQuestions().get(i)[0]);
			out.println("==========");
			for (Client client : Server.getClients()) {
				
				// contador de respuestas
				if (client != null){
					int answer = client.getAnswers().get(i);
					switch (answer) {
					case 1:
						first++;
						break;
					case 2:
						second++;
						break;
					case 3:
						third++;
						break;
					case 4:
						forth++;
						break;

					default:
						break;
					}
				}
					
			}
			out.println();
			out.println(Server.getQuestions().get(i)[1] + "-->" + first);
			out.println(Server.getQuestions().get(i)[2] + "-->" + second);
			out.println(Server.getQuestions().get(i)[3] + "-->" + third);
			out.println(Server.getQuestions().get(i)[4] + "-->" + forth);
		}
	}

}
