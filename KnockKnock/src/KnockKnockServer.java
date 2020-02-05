import java.net.*;
import java.io.*;

public class KnockKnockServer {

	public static void main(String[] args) {

		ClientID clientId = new ClientID();
		ServerSocket serverSocket = null;
		ThreadManager manager = new ThreadManager();

		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
			System.exit(1);
		}

		System.out.println("The Joke Server has Started!");

		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				JokeJob jokeJob = new JokeJob(clientSocket, clientId.assignID());
				manager.addJokeThread(jokeJob);
			} catch (IOException acceptClientSocket) {
				acceptClientSocket.printStackTrace();
				System.err.println("Client acception failed.");

				try {
					serverSocket.close();
				} catch (IOException serverSocketClose) {
					serverSocketClose.printStackTrace();
					System.err.println("The server socket was not closed.");
					System.exit(1);
				}
				System.exit(1);
			}
		}

	}
}