package server;

import java.util.ArrayList;

public class Client {
	// atributos
	private int id;
	private ArrayList<Integer> answers;
	
	// constructor
	public Client(int id) {
		this.id = id;
		answers = new ArrayList<>();
	}

	// getters and setters
	public int getId() {
		return id;
	}

	public ArrayList<Integer> getAnswers() {
		return answers;
	}

	
	
	// object
	@Override
	public String toString() {
		return "Client [id=" + id + ", answers=" + answers + "]";
	}
	
	

}
