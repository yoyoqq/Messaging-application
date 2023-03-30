package src.ClientServer;
// package ConnectServer.ClientHandler;

import java.io.*;
import java.net.*;
// import java.util.Scanner;

/**
 * This thread is responsible for reading user's input and send it
 * to the server.
 * It runs in an infinite loop until the user types 'bye' to quit.
 * Reference:
 * https://www.codejava.net/java-se/networking/how-to-create-a-chat-console-application-in-java-using-socket
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

    public void run() {
        Console console = System.console();

        // Send user data to the server -> name/ip/port
        String userData = client.userName + "/" + client.hostName + "/" + client.port;
        writer.println(userData);

        String text;

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