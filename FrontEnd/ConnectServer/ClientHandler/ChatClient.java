package User.ClientHandler;

import java.net.*;
import java.io.*;

/**
 * This is the chat client program.
 */
public class ChatClient {
    private String hostname;
    private int port;
    private String userName;

    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);

            InetAddress addr = socket.getInetAddress();
            System.out.println("connected to ip " + addr);

            System.out.println("Connected to the chat server");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getUserName() {
        return this.userName;
    }

    public static void main(String[] args) {
        // if (args.length < 2)
        // return;

        // String hostname = args[0];
        // int port = Integer.parseInt(args[1]);

        String hostname = "127.0.0.1";
        int port = 1234;

        // String hostname = "127.0.0.2";
        // int port = 1234;

        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }
}