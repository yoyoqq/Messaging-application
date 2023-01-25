import java.io.*;
import java.net.*;

class EchoServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server started");
        while (true) {
            Socket s = serverSocket.accept();
            InputStreamReader in = new InputStreamReader(s.getInputStream());
            // PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            // BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader bf = new BufferedReader(in);
            String str = bf.readLine();
            System.out.println(str);
            // String inputLine;
            // while ((inputLine = in.readLine()) != null) {
            //     out.println(inputLine);
            // }
        }
    }
}