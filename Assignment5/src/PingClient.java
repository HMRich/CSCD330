import java.io.*;
import java.net.*;
import java.util.*;

public class PingClient {
	private static final double LOSS_RATE = 0.3;
	private static final int AVERAGE_DELAY = 100;

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Required arguments: port");
			return;
		}
		
		String host = args[0];
		int serverPort = Integer.parseInt(args[1]);
		
		@SuppressWarnings("resource")
		DatagramSocket socket = new DatagramSocket(serverPort);

//		Random random = new Random();
		
		for(int index = 0; index < 10; index++)
		{
			
		}

		while (true) {
			// Create a datagram packet to hold incomming UDP packet.
			DatagramPacket request = new DatagramPacket(new byte[1024], 1024);

			// Block until the host receives a UDP packet.
			socket.receive(request);

			// Print the recieved data.
			printData(request);

			// Decide whether to reply, or simulate packet loss.
			if (random.nextDouble() < LOSS_RATE) {
				System.out.println("   Reply not sent.");
				continue;
			}

			// Simulate network delay.
			Thread.sleep((int) (random.nextDouble() * 2 * AVERAGE_DELAY));

			// Send reply.
			InetAddress clientHost = request.getAddress();
			int clientPort = request.getPort();
			byte[] buf = request.getData();
			DatagramPacket reply = new DatagramPacket(buf, buf.length, clientHost, clientPort);
			socket.send(reply);

			System.out.println("   Reply sent.");
		}
	}

	private static void printData(DatagramPacket request) throws Exception {
		// Obtain references to the packet's array of bytes.
		byte[] buf = request.getData();

		// Wrap the bytes in a byte array input stream,
		// so that you can read the data as a stream of bytes.
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);

		// Wrap the byte array output stream in an input stream reader,
		// so you can read the data as a stream of characters.
		InputStreamReader isr = new InputStreamReader(bais);

		// Wrap the input stream reader in a bufferred reader,
		// so you can read the character data a line at a time.
		// (A line is a sequence of chars terminated by any combination of \r and \n.)
		BufferedReader br = new BufferedReader(isr);

		// The message data is contained in a single line, so read this line.
		String line = br.readLine();

		// Print host address and data received from it.
		System.out.println("Received from " + request.getAddress().getHostAddress() + ": " + new String(line));
	}
}