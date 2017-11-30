import java.net.ServerSocket;
import java.net.Socket;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class WebServer {

	public static void main(String[] args) throws Exception {
		try (ServerSocket serverSocket = new ServerSocket(8080)) {
			System.out.println("Server Open.");
			while (true) {
				// accept client and setup input output stream
				Socket socket = serverSocket.accept();
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				PrintWriter pw = new PrintWriter(os, true);

				// get url from input string
				String inputString = br.readLine();
				String siteURL = "www" + inputString.split(" ")[1];

				Runnable server = () -> {
					File f;
					BufferedReader br2;
					String outputString;
					try {
						if (siteURL.equals("www/hello.html")) {

							// read file data
							f = new File("www/hello.html");
							br2 = new BufferedReader(new FileReader(f));
							pw.print("HTTP/1.1 200 OK\n" + "Content-type: text/html\n" + "Content-length: " + f.length() + "\n\r\n");
							System.out
									.println("HTTP/1.1 200 OK\n" + "Content-type: text/html\n" + "Content-length: " + f.length());
							System.out.println();
							outputString = br2.readLine();

							// output file
							while (outputString != null) {
								pw.print(outputString + "\r\n");
								System.out.println(outputString);
								outputString = br2.readLine();
							}

							pw.flush();
							pw.close();
							br2.close();
						} else {
							f = new File("www/notfound.html");
							br2 = new BufferedReader(new FileReader(f));
							pw.print("HTTP/1.1 404 Not Found\n" + "Content-type: text/html\n"
									+ "Content-length: 126\r\n\r\n");
							System.out.println(
									"HTTP/1.1 404 Not Found\n" + "Content-type: text/html\n" + "Content-length: 126\n");
							// System.out.println();
							outputString = br2.readLine();

							// output file
							while (outputString != null) {
								pw.print(outputString + "\r\n");
								System.out.println(outputString);
								outputString = br2.readLine();
							}

							pw.flush();
							pw.close();
							br2.close();
						}

					} catch (Exception e) {
						System.out.println("Error");
					}
				};
				Thread newThread = new Thread(server);
				newThread.start();
			}

		}
	}

}