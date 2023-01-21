import java.io.*;
import java.net.*;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        Socket echoSocket = new Socket("127.0.0.1", 8080);
        PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        out.println("Hello, Server!");
        System.out.println("Server: " + in.readLine());

        echoSocket.close();
        in.close();
        out.close();
        stdIn.close();
    }
}