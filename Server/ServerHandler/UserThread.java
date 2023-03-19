package Server.ServerHandler;

import java.io.*;
import java.net.*;
import java.util.Arrays;

import Server.Database.DatabaseProxy;

// import Server.Database.User;
// import Server.ServerHandler.API.GET;

/**
 * This thread handles connection for each connected client, so the server
 * can handle multiple clients at the same time.
 */
public class UserThread extends Thread {
    private String all_data;
    public int user_id;
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

            // connect user to database
            // proxy.putUser(serverMessage, clientMessage, MAX_PRIORITY)

            // Add user to database
            addUser(clientMessage = "", reader);
            System.out.println(this.all_data);
            // System.out.println(this.user_id);
            // writer.println(first_element);
            // String[] db_call = db_user_data.split(" ");
            // System.out.println(db_user_data.split(" ")[0]);
            // this.user_id = Integer.parseInt(db_call[0]);
            // writer.println(this.user_id);
            // System.out.println(this.user_id);

            // Integer.parseInt(data[2]));
            // reader.close();
            // output.write(null);
            // save data of the user to db
            // newUser(clientMessage);

            // // add user to groups
            // user.addToGroups();

            // send notification to everyone
            // server.broadcast("New user connected: " + user_id + "/" + clientMessage,
            // this);

            // create update function for everyone

            // Keep reading messages from the client and broadcasting them.
            do {
                // user input
                clientMessage = reader.readLine();
                if (clientMessage.charAt(0) != '/') {
                    continue;
                }
                String[] message = clientMessage.split("/");
                if (message.length == 0) {
                    continue;
                }
                // System.out.println(clientMessage);
                // for (String msg : message) {
                // System.out.println(msg);
                // }

                // follows the following syntax -> action/CRUD/...
                String action = message[1];
                // process data using API
                // writer.print(action);
                switch (action) {
                    // put statement
                    case "put":
                        String dataToPut = message[2];
                        switch (dataToPut) {
                            // handle put cases
                            case "newGroupChat":
                                this.proxy.putGroupChat(this.user_id);
                                break;
                            case "userInGroup":
                                try {
                                    int user_id = Integer.parseInt(message[3]);
                                    int groupchat_id = Integer.parseInt(message[4]);
                                    this.proxy.putUserInGroups(user_id, groupchat_id);
                                } catch (Exception e) {
                                    writer.println(e);
                                }
                                break;
                            case "message":
                                try {
                                    int groupChat_ID = Integer.parseInt(message[3]);
                                    int user_ID = Integer.parseInt(message[4]);
                                    String dateTime = message[5];
                                    String text = message[6];
                                    this.proxy.putMessage(groupChat_ID, user_ID, dateTime, text);
                                } catch (Exception e) {
                                    writer.println(e);
                                }
                                break;

                            default:
                                sendMessage("Invalid put statement");
                                break;
                        }
                        break;

                    // get statement
                    case "get":
                        String dataToGet = message[2];
                        switch (dataToGet) {
                            // handle get cases
                            case "users":
                                String all_users = this.proxy.getUsers();
                                writer.println(all_users);
                                break;
                            case "coordinator":
                                try {
                                    int groupchat_id = Integer.parseInt(message[3]);
                                    String coordinator = this.proxy.getCoordinator(groupchat_id);
                                    writer.println(coordinator);
                                } catch (Exception e) {
                                    writer.println(e);
                                }
                                break;
                            case "userInGroups":
                                try {
                                    int user_ID = Integer.parseInt(message[3]);
                                    String groups = this.proxy.getUserInGroups(user_ID);
                                    writer.println(groups);
                                } catch (Exception e) {
                                    writer.println(e);
                                }
                                break;
                            case "groupUsers":
                                try {
                                    int groupChat_ID = Integer.parseInt(message[3]);
                                    String members = this.proxy.getGroupUsers(groupChat_ID);
                                    writer.println(members);
                                } catch (Exception e) {
                                    writer.println(e);
                                }
                                break;
                            case "name":
                                try {
                                    int user_ID = Integer.parseInt(message[3]);
                                    String name = this.proxy.getName(user_ID);
                                    writer.println(name);
                                } catch (Exception e) {
                                    writer.println(e);
                                }
                                break;
                            case "messages":
                                try {
                                    int groupChat_ID = Integer.parseInt(message[3]);
                                    String messages = this.proxy.getMessage(groupChat_ID);
                                    writer.println(messages);
                                } catch (Exception e) {
                                    writer.println(e);
                                }
                                break;
                            case "messagesState":
                                try {
                                    int groupChat_ID = Integer.parseInt(message[3]);
                                    String messages = this.proxy.getMessageState(groupChat_ID);
                                    writer.println(messages);
                                } catch (Exception e) {
                                    writer.println(e);
                                }
                                break;
                            default:
                                sendMessage("Invalid get statement");
                                break;
                        }
                        break;

                    case "quit":
                        System.out.println("User quit");
                        writer.println("quit");
                        this.proxy.deleteUser(this.user_id, "User has quitted");
                        break;

                    default:
                        // handle default case
                        sendMessage("Invalid input");
                        break;
                }
            } while (!clientMessage.equals("bye"));

            // server.broadcast(clientMessage);
            // server.broadcast(Database.getUsersData());

            // server.removeUser(user_id, this);
            serverMessage = user_id + " has quitted.";
            // server.broadcast(serverMessage, this);
            // server.broadcast(serverMessage);
            socket.close();

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
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

    // int addUser() {
    // int user_id = ID_generator.user_id += 1;
    // // Database.userThreads.add(this);
    // return user_id;
    // }
}