package src.Server.ServerHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

import src.Server.Database.DatabaseProxy;

// Class created to interact with the server via commands 
public class API {
    private static String[] message;
    private DatabaseProxy proxy;
    private int user_id;

    public API(String[] message, DatabaseProxy proxy, int user_id) {
        API.message = message;
        this.proxy = proxy;
        this.user_id = user_id;
    }

    // return message from database
    public String getMessage() {
        try {
            return processMessage();
        } catch (Exception e) {
            return "400 Invalid message";
        }
    }

    private String processMessage() {

        // follows the following syntax -> action/CRUD/...
        String action = message[1];
        // process data using API
        switch (action) {
            // put statement
            case "put":
                String dataToPut = message[2];
                switch (dataToPut) {
                    // handle put cases
                    case "newGroupChat":
                        return this.proxy.putGroupChat(this.user_id);
                    case "userInGroup":
                        try {
                            int groupchat_id = Integer.parseInt(message[3]);
                            int user = Integer.parseInt(message[4]);
                            String result = this.proxy.putUserInGroups(user, groupchat_id, this.user_id);
                            return result;
                        } catch (Exception e) {
                            return "400 userInGroup";
                        }
                    case "message":
                        try {
                            int groupChat_ID = Integer.parseInt(message[3]);
                            // int user_ID = Integer.parseInt(message[4]);
                            int user_ID = this.user_id;
                            // String dateTime = message[5];
                            String dateTime = getDate();
                            String text = message[4];
                            String result = this.proxy.putMessage(groupChat_ID, user_ID, dateTime, text);
                            return result;
                        } catch (Exception e) {
                            return "400 message";
                        }

                    default:
                        return "Invalid put statement";
                }

                // get statement
            case "get":
                String dataToGet = message[2];
                switch (dataToGet) {
                    case "id":
                        return Integer.toString(this.user_id);
                    // handle get cases
                    case "users":
                        String all_users = this.proxy.getUsers();
                        // writer.println(all_users);
                        return all_users;
                    case "connectedUsers":
                        String connected_users = "";
                        for (UserThread user : ChatServer.userThreads) {
                            connected_users += user.getAllData() + "\n";
                        }
                        return connected_users;
                    case "coordinator":
                        try {
                            int groupchat_id = Integer.parseInt(message[3]);
                            String coordinator = this.proxy.getCoordinator(groupchat_id);
                            // writer.println(coordinator);
                            return coordinator;
                        } catch (Exception e) {
                            // writer.println(e);
                            return "400 coordinator";
                        }
                    case "userInGroups":
                        try {
                            int user_ID = Integer.parseInt(message[3]);
                            String groups = this.proxy.getUserInGroups(user_ID);
                            // writer.println(groups);
                            return groups;
                        } catch (Exception e) {
                            // writer.println(e);
                            return "400 userInGroups";
                        }
                    case "groupMembers":
                        try {
                            int groupChat_ID = Integer.parseInt(message[3]);
                            String members = this.proxy.getMembersOfGroup(groupChat_ID);
                            // writer.println(members);
                            return members;
                        } catch (Exception e) {
                            // writer.println(e);
                            return "400 groupUsers";
                        }
                    case "name":
                        try {
                            int user_ID = Integer.parseInt(message[3]);
                            String name = this.proxy.getName(user_ID);
                            // writer.println(name);
                            return name;
                        } catch (Exception e) {
                            // writer.println(e);
                            return "400 name";
                        }
                    case "messages":
                        try {
                            int groupChat_ID = Integer.parseInt(message[3]);
                            String messages = this.proxy.getMessage(groupChat_ID, this.user_id);
                            // writer.println(messages);
                            return messages;
                        } catch (Exception e) {
                            // writer.println(e);
                            return "400 messages";
                        }

                    default:
                        return "Invalid get statement";
                }
            case "kickUser":
                try {
                    int groupChat_ID = Integer.parseInt(message[2]);
                    int userID = Integer.parseInt(message[3]);
                    String message = this.proxy.deleteUserFromGroupChat(groupChat_ID, userID, this.user_id);
                    return message;
                } catch (Exception e) {
                    // writer.println(e);
                    return "400 kickUser";
                }
            case "quit":
                System.out.println("User quit");
                return "Server: user " + this.user_id + " has quit.";

            case "help":
                return commands();

            default:
                // handle default case
                return "Type 'help'";
        }
    }

    // return date
    private String getDate() {
        Date date = new Date(); // create a new Date object with the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // create a SimpleDateFormat object with the
        String formattedDate = sdf.format(date); // format the date using the SimpleDateFormat object
        return formattedDate;
    }

    private String commands() {
        String helpText = "Available commands:\n" +
                "/put/newGroupChat - Create a new group chat\n" +
                "/put/userInGroup/user_id/groupchat_id - Add a user to a group chat\n"
                + "/put/message/groupChat_ID/user_ID/dateTime/text - Send a message to a group chat\n" +
                "/get/users - Get a list of all users\n" +
                "/get/coordinator/groupchat_id - Get the coordinator of a group chat\n" +
                "/get/userInGroups/user_ID - Get the groups a user is in\n" +
                "/get/groupUsers/groupChat_ID - Get the users in a group chat\n" +
                "/get/name/user_ID - Get the name of a user\n" +
                "/get/messages/groupChat_ID - Get the messages in a group chat\n" +
                "/get/messagesState/groupChat_ID - Get the message states in a group chat\n"
                +
                "/quit - Quit the application\n" +
                "/help - Display this help message";
        return helpText;
    }

    public static void main(String[] args) {
        DatabaseProxy proxy = DatabaseProxy.getInstance();
        String[] command;

        // CREATE GROUPCHATS
        command = "/put/newGroupChat".split("/");
        new API(command, proxy, 1).getMessage();
        command = "/put/userInGroup/1/2".split("/");
        new API(command, proxy, 1).getMessage();
        command = "/put/userInGroup/1/3".split("/");
        new API(command, proxy, 1).getMessage();
        command = "/put/userInGroup/1/4".split("/");
        new API(command, proxy, 1).getMessage();
        command = "/put/userInGroup/1/5".split("/");
        new API(command, proxy, 1).getMessage();

        // command = "/put/newGroupChat".split("/");
        // new API(command, proxy, 2).getMessage();
        // command = "/put/userInGroup/2/3".split("/");
        // new API(command, proxy, 2).getMessage();
        // command = "/put/userInGroup/2/4".split("/");
        // new API(command, proxy, 2).getMessage();

        // command = "/put/newGroupChat".split("/");
        // new API(command, proxy, 4).getMessage();
        // command = "/put/userInGroup/3/3".split("/");
        // new API(command, proxy, 4).getMessage();

        // // PUT MESSAGES INTO GROUPS
        // command = "/put/message/1/Hi, I am the coordinator".split("/");
        // new API(command, proxy, 1).getMessage();
        // command = "/put/message/1/Hello Alice, I am Bob nice to meet you".split("/");
        // new API(command, proxy, 2).getMessage();
        // command = "/put/message/1/Hello everyone".split("/");
        // new API(command, proxy, 5).getMessage();

        // command = "/put/message/2/Welcome everyone to the group 2".split("/");
        // new API(command, proxy, 2).getMessage();
        // command = "/put/message/2/Thank you".split("/");
        // new API(command, proxy, 3).getMessage();
        // command = "/put/message/2/Thank you".split("/");
        // new API(command, proxy, 4).getMessage();

        // command = "/get/messages/1".split("/");
        // String a = new API(command, proxy, 1).getMessage();
        // System.out.println(a);
    }
}
