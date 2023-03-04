package Server.ServerHandler;

import java.io.*;
import java.net.*;
import java.util.*;
import Server.Database.Database;

/**
 * This is the chat server program.
 * Press Ctrl + C to terminate the program.
 */
public class ChatServer {
    private int port;

    public ChatServer(int port) {
        this.port = port;
    }

    // Run server
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("Chat Server is listening on IP " + address.getHostAddress());
            System.out.println("Chat Server is listening on port " + port);

            // New user connected
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(socket, this);
                Database.userThreads.add(newUser);
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
        for (UserThread aUser : Database.userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Stores username of the newly connected client.
     */
    void addUserName(String userName) {
        Database.userNames.add(userName);
    }

    /**
     * When a client is disconneted, removes the associated username and UserThread
     * CHANGE TO USER_THREAD.JAVA FILE TO CLOSE SOCKET/ INPUTSTREAMREADER/
     */
    void removeUser(String userName, UserThread aUser) {
        boolean removed = Database.userNames.remove(userName);
        if (removed) {
            Database.userThreads.remove(aUser);
            System.out.println("The user " + userName + " quitted");
        }
        // close socket/ inputStreamReader/ outputStreamWriter/ bufferedReader/
        // bufferedWriter
        // finally{
        // // close all connections
        // try {
        // if (socket != null){
        // socket.close();
        // }
        // if (inputStreamReader != null){
        // inputStreamReader.close();
        // }
        // if (outputStreamWriter != null){
        // outputStreamWriter.close();
        // }
        // if (bufferedReader != null){
        // bufferedReader.close();
        // }
        // if (bufferedWriter != null){
        // bufferedWriter.close();
        // }
        // }
        // catch (IOException e){
        // e.printStackTrace();
        // }
    }

    Set<String> getUserNames() {
        return Database.userNames;
    }

    /**
     * Returns true if there are other users connected (not count the currently
     * connected user)
     */
    boolean hasUsers() {
        return !Database.userNames.isEmpty();
    }
}