package src.Server.ServerHandler;

import java.io.*;
import java.net.*;
import java.util.*;

import src.Server.Database.DatabaseProxy;

/**
 * This is the chat server program.
 * Press Ctrl + C to terminate the program.
 */
public class ChatServer {
    private int port;
    private InetAddress address;
    DatabaseProxy proxy = DatabaseProxy.getInstance(); // create instance of the singleton database
    static HashSet<UserThread> userThreads = new HashSet<>();
    Coordinator coordinator;

    public ChatServer(int port) {
        this.port = port;
        coordinator = new Coordinator(this.proxy);
    }

    // Run server
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            address = InetAddress.getLocalHost();
            System.out.println("Chat Server is listening on IP " + address.getHostAddress());
            System.out.println("Chat Server is listening on port " + port);

            // New user connected
            while (true) {
                Socket socket = serverSocket.accept();
                // System.out.println("New user connected");

                // int user_id = ID_generator.user_id += 1;
                UserThread newUser = new UserThread(socket, this, this.proxy);
                userThreads.add(newUser);
                newUser.start();

            }
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Delivers a message from one user to others (broadcasting)
     */
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : ChatServer.userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    void printData(String data) {
        System.out.println(data);
    }
}