package src.Server.ServerHandler;

import java.io.*;
import java.net.*;

import src.Server.Database.DatabaseProxy;

// import Server.Database.User;
// import Server.ServerHandler.API.GET;

/**
 * This thread handles connection for each connected client, so the server
 * can handle multiple clients at the same time.
 */
public class UserThread extends Thread {
    private String all_data;
    private int user_id;
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    DatabaseProxy proxy;
    // private DatabaseProxy proxy;
    // private User user;

    // api
    // private GET apiGet = new GET();

    public UserThread(Socket socket, ChatServer server, DatabaseProxy proxy) {
        // this.user_id = ID_generator.getUserID();
        this.socket = socket;
        this.server = server;
        this.proxy = proxy;
        // this.proxy = proxy; // database proxy
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
            String serverMessage;
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
                // System.out.println(message);
                // System.out.println(result);
                sendMessage(result);
                // writer.write(result);
                // writer.flush();
            } while (!clientMessage.equals("exit"));

            // server.broadcast(clientMessage);
            // server.broadcast(Database.getUsersData());

            // server.removeUser(user_id, this);
            // serverMessage = user_id + " has quitted.";
            // server.broadcast(serverMessage, this);
            // server.broadcast(serverMessage);
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
        // System.out.println(db_user_data_array[0]);
        // System.out.println(first_element);
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
    // add new user when connects
    // void newUser(String clientMessage){
    // String[] splitData = clientMessage.split("/");
    // user = new User(user_id, splitData[0], splitData[1], splitData[2]);
    // Database.usersData.add(user);
    // }
    /**
     * Sends a list of online users to the newly connected user.
     */
    // void printUsers() {
    // if (server.hasUsers()) {
    // writer.println("Connected users: " + server.getUserNames());
    // } else {
    // writer.println("No other users connected");
    // }
    // }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }

    int getID() {
        return this.user_id;
    }
    // String getDate() {
    // Date date = new Date(); // create a new Date object with the current date and
    // time
    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // create
    // a SimpleDateFormat object with the
    // String formattedDate = sdf.format(date); // format the date using the
    // SimpleDateFormat object
    // return formattedDate;
    // }
    // int addUser() {
    // int user_id = ID_generator.user_id += 1;
    // // Database.userThreads.add(this);
    // return user_id;
    // }
}