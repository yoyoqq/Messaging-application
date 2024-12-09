package src.ClientServer;

import java.net.*;
import java.io.*;

/**
 * This is the chat client program. It handles the connection of the client
 * connecting it to a IP and port.
 * Reference:
 * https://www.codejava.net/java-se/networking/how-to-create-a-chat-console-application-in-java-using-socket
 */
public class ChatClient {
    // User data
    public String userName;
    public String hostName;
    public int port;

    public ChatClient(String userName, String hostName, int port) {
        this.userName = userName;
        this.hostName = hostName;
        this.port = port;
    }

    public void execute() {
        // establish a connection
        try {
            Socket socket = new Socket(hostName, port);

            // handle the input and output on a different thread
            new WriteThread(socket, this).start();
            new ReadThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public String getUserName() {
        return userName;
    }
}