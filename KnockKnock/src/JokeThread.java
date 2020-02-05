import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class JokeThread extends Thread {

	private JokeJob mJob;

	public JokeThread(JokeJob jokeJob) {
		mJob = jokeJob;
		this.start();
	}

	public synchronized void run() {
		PrintWriter out;
		try {
			out = new PrintWriter(mJob.getSocket().getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mJob.getSocket().getInputStream()));
			String inputLine, outputLine;
			KnockKnockProtocol kkp = new KnockKnockProtocol();

			outputLine = kkp.processInput(null);
			out.println(outputLine);

			while ((inputLine = in.readLine()) != null) {
				outputLine = kkp.processInput(inputLine);
				out.println(outputLine);
				if (outputLine.equals("Bye."))
					break;
			}
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			mJob.getSocket().close();
		} catch (IOException jobSocket) {
			jobSocket.printStackTrace();
			System.exit(1);
		}
	}
}
