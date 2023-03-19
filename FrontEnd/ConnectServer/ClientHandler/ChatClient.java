package ClientHandler;
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
            // addr = socket.getInetAddress();
            // System.out.println("this is your data" + ID + " " + hostname + " "+ port +" "
            // + userName +" "+ addr);
            // InetAddress addr = socket.getInetAddress();
            // System.out.println("connected to ip " + addr);

            // System.out.println("Connected to the chat server");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    // void setUserName(String userName) {
    // this.userName = userName;
    // }

    public String getUserName() {
        return userName;
    }
}