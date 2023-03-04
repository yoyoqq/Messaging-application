package Progress;
import java.io.*;
import java.net.*;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("127.0.0.1", 8080);
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        out.println("connected");
        out.flush();
        // BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        // BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        // out.println("Hello, Server!");
        // out.println("aasdfasdf");
        // System.out.println("Server: " + in.readLine());

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
        System.out.println("server" + str);

        // echoSocket.close();
        in.close();
        out.close();
        // stdIn.close();
        
    }
} 