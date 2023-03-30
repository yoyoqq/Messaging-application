package src.Server.Database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Singleton pattern
public class DatabaseProxy implements Data {
    // private constructor
    private DatabaseProxy() {
        // initialization code here
    }

    // private static variable of the same type as the class
    private static DatabaseProxy instance;
    // private static variable to hold the singleton instance of the Database class
    private static Database database;

    // public static method that returns the single instance of the class
    public static synchronized DatabaseProxy getInstance() {
        if (instance == null) {
            instance = new DatabaseProxy();
        }
        return instance;
    }

    // public method to get the singleton instance of the Database class
    public Database getDatabase() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    /*
     * @param name, ip and port of the user
     * 
     * @return user information
     */
    public String putUser(String name, String ip, int port) {
        // if user already exists
        String isUser = getDatabase().findUser(name, ip, port);
        if (!isUser.equals("-1")) {
            return isUser;
        } else {
            // create new user
            return getDatabase().putUser(name, ip, port);
        }
    }

    /*
     * @param user_id for the new gropuchat
     */
    public String putGroupChat(int coordinator) {
        String groupChat_ID = getDatabase().putGroupChat(coordinator); // create groupchat and assign to it
        return "GroupChat_ID: " + groupChat_ID;
    }

    /*
     * Put a user in group_ID
     */
    public String putUserInGroups(int user_ID, int groupChat_ID, int coordinatorId) {
        if (getDatabase().ifUserIsCoordinator(groupChat_ID, coordinatorId)) {
            getDatabase().putUserInGroups(user_ID, groupChat_ID);
            return "User " + user_ID + " added to the groupchat " + groupChat_ID;
        } else {
            return "You are not the coordinator";
        }

    }

    /*
     * send message to a groupchat
     * 
     * @return message confirmation sent to the server
     */
    public String putMessage(int groupChat_ID, int user_ID, String dateTime, String text) {
        if (getDatabase().ifUserInGroup(user_ID, groupChat_ID)) {
            getDatabase().putMessage(groupChat_ID, user_ID, dateTime, text);
            return "";
        } else {
            return "You are not in the groupchat " + groupChat_ID;
        }
    }

    /*
     * @return all the users id, name, ip and port
     */
    public String getUsers() {
        return getDatabase().getUsers();
    }

    public String getGroupChats() {
        return getDatabase().getGroupChats();
    }

    /*
     * @return coordinator of a groupchat
     */
    public String getCoordinator(int groupChat_ID) {
        return getDatabase().getCoordinator(groupChat_ID);
    }

    /*
     * @return the groups that a user is in
     */
    public String getUserInGroups(int user_ID) {
        return getDatabase().getUserInGroups(user_ID);
    }

    /*
     * @return members of a group
     */
    public String getGroupUsers(int groupChat_ID) {
        return getDatabase().getGroupUsers(groupChat_ID);

    }

    public String getMembersOfGroup(int gropuChat_ID) {
        String result = "Members in groupChat_ID: " + gropuChat_ID + "\n";
        String[] users = getDatabase().getGroupUsers(gropuChat_ID).split("/");
        for (String u : users) {
            result += "ID: " + u + "\t" + getName(Integer.parseInt(u)) + "\n";
        }
        return result;
    }

    /*
     * @return get name from ID
     */
    public String getName(int user_ID) {
        return getDatabase().getName(user_ID);
    }

    /*
     * @return messages from a groupchat
     * groupID, name, time, message
     */
    public String getMessage(int groupChat_ID, int userId) {
        if (!getDatabase().ifUserInGroup(userId, groupChat_ID)) {
            return "You are not in the GroupChat";
        }
        // mark as read
        getDatabase().updateMessageState(groupChat_ID, userId);
        // get Message state
        Map<String, List<List<String>>> state = getState(groupChat_ID);
        return processMessage(groupChat_ID, state);

    }

    private String processMessage(int groupChat_ID, Map<String, List<List<String>>> state) {
        // get the message
        String rawMessages = getDatabase().getMessage(groupChat_ID);
        // split the messages
        String[] messages = rawMessages.split("/");
        // format the messages
        StringBuilder formattedMessages = new StringBuilder();
        for (int i = 0; i < messages.length; i++) {
            String message = messages[i];
            String[] parts = message.split(" ");
            String messageID = parts[0];
            String messageReadBy = formatMessageReadBy(state, messageID);
            String name = parts[1];
            String time = parts[2];
            String text = String.join(" ", Arrays.copyOfRange(parts, 3, parts.length));
            formattedMessages.append(String.format("%-8s (%s): %-30s %s %n", name, time, text, messageReadBy));
        }
        return formattedMessages.toString();
    }

    private String formatMessageReadBy(Map<String, List<List<String>>> map, String messageID) {
        String messageRead = "";
        String messageNotRead = "";
        if (map.containsKey(messageID)) {
            List<List<String>> data = map.get(messageID);
            List<String> readBy = data.get(0);
            List<String> notReadBy = data.get(1);
            for (String i : readBy) {
                messageRead += i + " ";
            }
            for (String i : notReadBy) {
                messageNotRead += i + " ";
            }
        }
        String result = String.format("(Message read by: %s)(Message not read by: %s)", messageRead, messageNotRead);
        return result;
    }

    private Map<String, List<List<String>>> getState(int groupChat_ID) {
        String[] messageState = getDatabase().getMessageState(groupChat_ID).split("/");

        Map<String, List<List<String>>> map = new HashMap<>();
        for (String block : messageState) {
            String[] parts = block.split(" ");
            String messageId = parts[0];
            String user = parts[1];
            String state = parts[2];
            if (!map.containsKey(messageId)) {
                map.put(messageId, new ArrayList<>());
                map.get(messageId).add(new ArrayList<>());
                map.get(messageId).add(new ArrayList<>());
            }
            if (state.equals("true")) {
                map.get(messageId).get(0).add(user);
            } else {
                map.get(messageId).get(1).add(user);
            }
        }
        return map;
    }

    /*
     * @return message ID's from a groupchat
     */
    public String get_messageID_from_Groupchat(int groupChat_ID) {
        return getDatabase().get_messageID_from_Groupchat(groupChat_ID);
    }

    /**
     * @param groupChat_ID
     * @return message state from a gropuchat
     */
    public String getMessageState(int groupChat_ID) {
        return getDatabase().getMessageState(groupChat_ID);
    }

    public String updateCoordinator(int groupChat_ID, int user_ID) {
        return getDatabase().updadateCoordinator(groupChat_ID, user_ID);
    }

    public String deleteUserFromGroupChat(int groupChat_ID, int user_ID, int coordinator) {
        if (!getDatabase().ifUserIsCoordinator(groupChat_ID, coordinator)) {
            return "You are not the coordinator";
        }
        getDatabase().deleteUserInGroup(user_ID, groupChat_ID);
        return "Successfully removed";
    }

    public static void main(String[] args) {
        // DatabaseProxy proxy = DatabaseProxy.getInstance();
        // proxy.putUserInGroups(1, 30);
        // String a = proxy.putMessage(1, 1, "10:30", "hello worlsd");
        // System.out.println(a);
        // a = proxy.putMessage(1, 2, "12", "testfor double");
        // System.out.println(a);
        // String a = proxy.getMessage(1, 3);
        // System.out.println(a);
        // String a = proxy.putUser("bob", "10.0.0.2", 9876);
        // System.out.println(a);
        // proxy.putMessage(1, 1, "10:30", "hey im user one");

        // when read a message, put mark as read
        // String a = proxy.getMessage(1, 1);
        // System.out.println(a);
    }
}
