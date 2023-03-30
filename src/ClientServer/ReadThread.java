package src.ClientServer;

import java.io.*;
import java.net.*;

/**
 * This thread is responsible for reading server's input and printing it
 * to the console.
 * It runs in an infinite loop until the client disconnects from the server.
 *
 */
public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;

    public ReadThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            // ex.printStackTrace();
        }
    }

    // run till the client closes the connection
    public void run() {
        String response;

        while (true) {
            try {
                response = reader.readLine();
                System.out.println(response);

            } catch (Exception ex) {
                System.out.println("ReadThread: Server closed");
                break;
            }
        }
    }
}