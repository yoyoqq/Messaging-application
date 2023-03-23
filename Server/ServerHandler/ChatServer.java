package Server.ServerHandler;

import java.io.*;
import java.net.*;
import java.util.*;
import Server.Database.DatabaseProxy;

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
        // coordinator = new Coordinator();
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
    // }
    // }

    // void broadcast(String message) {
    // for (UserThread aUser : SQLiteJDBC.userThreads) {
    // aUser.sendMessage(message);
    // }
    // }
    // void broadcast(ArrayList message){
    // for (UserThread aUser : Database.userThreads) {
    // aUser.sendMessage(message);
    // }
    // }

    void printData(String data) {
        System.out.println(data);
    }

    /**
     * Stores username of the newly connected client.
     */
    // void addUserName(String userName) {
    // Database.userNames.add(userName);
    // }

    /**
     * When a client is disconneted, removes the associated username and UserThread
     * CHANGE TO USER_THREAD.JAVA FILE TO CLOSE SOCKET/ INPUTSTREAMREADER/
     */
    // void removeUser(String userName, UserThread aUser) {
    // boolean removed = Database.userNames.remove(userName);
    // if (removed) {
    // Database.userThreads.remove(aUser);
    // System.out.println("The user " + userName + " quitted");
    // }
    // close socket/ inputStreamReader/ outputStreamWriter/ bufferedReader/
    // bufferedWriter
    // finally{
    // // close all connections
    // finally{
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
    // }
    // }
    // void removeUser(Integer user_id, UserThread aUser) {
    // boolean removed = Database.userNames.remove(user_id);
    // if (removed) {
    // Database.userThreads.remove(aUser);
    // System.out.println("The user " + user_id + " quitted");
    // }
    // }

    // Set<String> getUserNames() {
    // return Database.userNames;
    // }

    /**
     * Returns true if there are other users connected (not count the currently
     * connected user)
     */
    // boolean hasUsers() {
    // return !Database.userNames.isEmpty();
    // }
}