
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPServer {

	public static void main(String[] args) throws IOException, Exception {
		Socket client;
		PrintWriter printwriter;

		client = new Socket("192.168.43.156", 9191); // connect to server
		printwriter = new PrintWriter(client.getOutputStream(), true);
		
		String msg = "HI FROM JAVA APP";
		
		for(int i=0; i<1000; i++) {
			printwriter.write(msg + String.valueOf(i) + "\n"); // write the message to output stream
			printwriter.flush();
		}

		printwriter.close();
		client.close(); // closing the connection
	}
}