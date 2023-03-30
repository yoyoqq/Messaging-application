package src.ClientServer;
// package ConnectServer.ClientHandler;

import java.io.*;
import java.net.*;
// import java.util.Scanner;

/**
 * This thread is responsible for reading user's input and send it
 * to the server.
 * It runs in an infinite loop until the user types 'bye' to quit.
 *
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
        // Scanner sc = new Scanner(System.in);
        // String userName = console.readLine("\nEnter your name: ");
        // client.setUserName(userName);
        // writer.println(userName);
        // writer.println(socket.getInetAddress().getAddress());

        // Send user data to the server -> name/ip/port
        String userData = client.userName + "/" + client.hostName + "/" + client.port;
        // System.out.println(userData);
        // writer.write(userData);
        // writer.flush();
        writer.println(userData);

        String text;

        do {
            // text = console.readLine("[" + client.getUserName() + "]: ");
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