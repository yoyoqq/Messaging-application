package Server.ServerHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import Server.Database.DatabaseProxy;

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
        return processMessage();
    }

    private String processMessage() {
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
                            return "400 userInGroup";
                        }
                        break;
                    case "message":
                        try {
                            int groupChat_ID = Integer.parseInt(message[3]);
                            // int user_ID = Integer.parseInt(message[4]);
                            int user_ID = this.user_id;
                            // String dateTime = message[5];
                            String dateTime = getDate();
                            String text = message[4];
                            this.proxy.putMessage(groupChat_ID, user_ID, dateTime, text);
                        } catch (Exception e) {
                            return "400 message";
                        }
                        break;

                    default:
                        return "Invalid put statement";
                }
                break;

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
                    case "groupUsers":
                        try {
                            int groupChat_ID = Integer.parseInt(message[3]);
                            String members = this.proxy.getGroupUsers(groupChat_ID);
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
                            String messages = this.proxy.getMessage(groupChat_ID);
                            // writer.println(messages);
                            return messages;
                        } catch (Exception e) {
                            // writer.println(e);
                            return "400 messages";
                        }
                    case "messagesState":
                        try {
                            int groupChat_ID = Integer.parseInt(message[3]);
                            String messages = this.proxy.getMessageState(groupChat_ID);
                            // writer.println(messages);
                            return messages;
                        } catch (Exception e) {
                            // writer.println(e);
                            return "400 messagesState";
                        }
                    default:
                        return "Invalid get statement";
                }

            case "quit":
                System.out.println("User quit");
                // writer.println("quit");
                return "Server: user " + this.user_id + " has quit.";
            // this.proxy.deleteUser(this.user_id, "User has quitted");

            default:
                // handle default case
                return "Invalid input";
        }
        return "200";
    }

    // void sendMessage(String message) {
    // writer.println(message);
    // }

    String getDate() {
        Date date = new Date(); // create a new Date object with the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // create a SimpleDateFormat object with the
        String formattedDate = sdf.format(date); // format the date using the SimpleDateFormat object
        return formattedDate;
    }

    public static void main(String[] args) {

    }
}
