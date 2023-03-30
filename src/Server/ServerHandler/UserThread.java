package src.Server.ServerHandler;

import java.io.*;
import java.net.*;

import src.Server.Database.DatabaseProxy;

/**
 * This thread handles connection for each connected client, so the server
 * can handle multiple clients at the same time.
 */
public class UserThread extends Thread {
    private String all_data;
    private int user_id;
    private Socket socket;
    // private ChatServer server;
    private PrintWriter writer;
    DatabaseProxy proxy;

    public UserThread(Socket socket, ChatServer server, DatabaseProxy proxy) {
        this.socket = socket;
        this.proxy = proxy;
    }

    // Run a multithread instance while the client exists.
    @Override
    public void run() {
        try {
            // Get input and output streams.
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
            String clientMessage;

            // Add user to database
            addUser(clientMessage = "", reader);
            System.out.println(this.all_data);

            // Keep reading messages from the client and broadcasting them.
            do {
                // user input
                clientMessage = reader.readLine();

                if (!validateMessage(clientMessage)) {
                    continue;
                }
                String[] message = clientMessage.split("/");
                String result = new API(message, this.proxy, this.user_id).getMessage();
                sendMessage(result);

            } while (!clientMessage.equals("exit"));

            removeUser();
            socket.close();

        } catch (Exception ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            // ex.printStackTrace();
            removeUser();
        }
    }

    // remove user from server
    void removeUser() {
        ChatServer.userThreads.remove(this);
    }

    // add user to database
    void addUser(String clientMessage, BufferedReader reader) throws IOException {
        clientMessage = reader.readLine();
        String[] data = clientMessage.split("/");
        String db_user_data = proxy.putUser(data[0], data[1], Integer.parseInt(data[2]));
        String[] db_user_data_array = db_user_data.split(" ");
        String first_element = db_user_data_array[0];

        this.user_id = Integer.parseInt(first_element);
        this.all_data = db_user_data;
    }

    boolean validateMessage(String clientMessage) {
        if (clientMessage.length() == 0)
            return false;
        if (clientMessage.charAt(0) != '/') {
            return false;
        }
        String[] message = clientMessage.split("/");
        if (message.length == 0) {
            return false;
        }
        return true;
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }

    int getID() {
        return this.user_id;
    }

    String getAllData() {
        return this.all_data;
    }
}