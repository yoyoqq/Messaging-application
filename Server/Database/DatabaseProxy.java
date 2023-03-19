package Server.Database;

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
        return getDatabase().putUser(name, ip, port);
    }

    /*
     * Select who's gonna be the next coordinator
     */
    public void putGroupChat(int coordinator) {
        getDatabase().putGroupChat(coordinator);
    }

    /*
     * Put a user in group_ID
     */
    public void putUserInGroups(int user_ID, int groupChat_ID) {
        getDatabase().putUserInGroups(user_ID, groupChat_ID);
    }

    /*
     * send message to a groupchat
     * 
     * @return message confirmation sent to the server
     */
    public boolean putMessage(int groupChat_ID, int user_ID, String dateTime, String text) {
        return getDatabase().putMessage(groupChat_ID, user_ID, dateTime, text);
    }

    // void putMessageState(int user_ID, int message_ID, int groupChat_ID, boolean
    // bool) {
    // getDatabase().putMessageState(user_ID, message_ID, groupChat_ID, bool);
    // }

    /*
     * @return all the users id, name, ip and port
     */
    public String getUsers() {
        return getDatabase().getUsers();
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
    public String getMessage(int groupChat_ID) {
        return getDatabase().getMessage(groupChat_ID);
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

    // delete user from database and notify all the members
    public void deleteUser(int user_ID, String message) {
        getDatabase().deleteUser(user_ID, message);
    }

    // other methods and properties here
    public static void main(String[] args) {
        // DatabaseProxy proxy = DatabaseProxy.getInstance();
    }
}
