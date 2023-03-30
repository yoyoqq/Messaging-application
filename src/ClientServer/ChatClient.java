package src.ClientServer;
// package ConnectServer.ClientHandler;

import java.net.*;
import java.io.*;

/**
 * This is the chat client program.
 */
public class ChatClient {
    // User data
    // private int ID;
    public String userName;
    public String hostName;
    public int port;
    // private InetAddress addr;

    public ChatClient(String userName, String hostName, int port) {
        this.userName = userName;
        this.hostName = hostName;
        this.port = port;
    }

    public void execute() {
        try {
            Socket socket = new Socket(hostName, port);

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