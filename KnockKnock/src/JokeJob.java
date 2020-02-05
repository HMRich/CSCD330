import java.net.Socket;

public class JokeJob {

	private int mClientId;
	private Socket mSocket;

	public JokeJob(Socket socket, int clientId) {
		mClientId = clientId;
		mSocket = socket;
	}

	public Socket getSocket() {
		return mSocket;
	}

	public int getClientId() {
		return mClientId;
	}

}