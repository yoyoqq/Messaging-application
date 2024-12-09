package src.ClientServer;
// package ConnectServer.ClientHandler;

import java.io.*;
import java.net.*;
// import java.util.Scanner;

/**
 * This thread is responsible for reading user's input and send it
 * to the server.
 * It runs in an infinite loop until the user types 'bye' to quit.
 */
public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // run till the client closes the connection
    public void run() {
        Console console = System.console();

        // Send user data to the server -> name/ip/port
        String userData = client.userName + "/" + client.hostName + "/" + client.port;
        writer.println(userData);

        String text;

        // output data to the server
        do {
            text = console.readLine();
            writer.println(text);

        } while (!text.equals("exit"));

        try {
            socket.close();
        } catch (Exception ex) {
            System.out.println("Error writing to server: " + ex.getMessage());

        }
    }
}