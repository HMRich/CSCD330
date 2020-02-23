import java.io.*;
import java.net.*;
import java.util.*;

public final class WebServer
{
	public static void main(String argv[]) throws Exception
	{
		int portNumber = 6789;

		ServerSocket listener = new ServerSocket(portNumber);

		while(!listener.isClosed())
		{
			Socket socket = listener.accept();

			HttpRequest request = new HttpRequest(socket);

			Thread thread = new Thread(request);

			thread.start();

		}
		listener.close();
	}
}

final class HttpRequest implements Runnable
{
	final static String CRLF = "\r\n";
	Socket socket;

	public HttpRequest(Socket socket) throws Exception
	{
		this.socket = socket;
	}

	public void run()
	{
		try
		{
			processRequest();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
		}
	}

	private void processRequest() throws Exception
	{
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		DataOutputStream dos = new DataOutputStream(os);

		String requestLine = br.readLine();


		System.out.println();
		System.out.println(requestLine);

		String headerLine = null;
		while((headerLine = br.readLine()).length() != 0)
		{
			System.out.println(headerLine);
		}

		StringTokenizer tokens = new StringTokenizer(requestLine);
		tokens.nextToken();
		String fileName = tokens.nextToken();


		fileName = "." + fileName;

		FileInputStream fis = null;
		boolean fileExists = true;
		try
		{
			fis = new FileInputStream(fileName);
		}
		catch(FileNotFoundException e)
		{
			fileExists = false;
		}

		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;
		if(fileExists)
		{
			statusLine = "HTTP/1.1 200 OK";
			contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
		}
		else
		{
			statusLine = "HTTP/1.1 404 Not Found";
			contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
			entityBody = "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>" + "<BODY>Not Found</BODY></HTML>";
		}

		dos.writeBytes(statusLine);


		dos.writeBytes(contentTypeLine);


		dos.writeBytes(CRLF);

		if(fileExists)
		{
			sendBytes(fis, os);
			fis.close();
		}
		else
		{
			dos.writeBytes(entityBody);
		}


		os.close();
		dos.close();
		br.close();
		socket.close();
	}

	private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception
	{
		byte[] buffer = new byte[1024];
		int bytes = 0;

		while((bytes = fis.read(buffer)) != -1)
		{
			os.write(buffer, 0, bytes);
		}
	}

	private static String contentType(String fileName)
	{
		if(fileName.endsWith(".htm") || fileName.endsWith(".html"))
		{
			return "text/html";
		}
		if(fileName.endsWith(".gif"))
		{
			return "image/gif";
		}
		if(fileName.endsWith(".jpeg"))
		{
			return "image/jpeg";
		}
		return "application/octet-stream";
	}

}