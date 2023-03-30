package src.Server.Database;

public interface Data {
    public static String putUser(String name, String ip, int port) {
        return null;
    };

    public static void putGroupChat(int coordinator) {
    };

    static void putUserInGroups(int user_ID, int groupChat_ID) {
    };

    static void putMessage(int groupChat_ID, int user_ID, String dateTime, String text) {
    };

    static void putMessageState(int user_ID, int message_ID, int groupChat_ID, boolean bool) {
    };

    static String getUsers() {
        return null;
    };

    //
    static String getCoordinator(int groupChat_ID) {
        return null;
    };

    static String getUserInGroups(int user_ID) {
        return null;
    };

    static String getGroupUsers(int groupChat_ID) {
        return null;
    };

    static String getName(int user_ID) {
        return null;
    };

    static String getMessage(int groupChat_ID) {
        return null;
    };

    static String get_messageID_from_Groupchat(int groupChat_ID) {
        return null;
    };

    static String getMessageState(int groupChat_ID) {
        return null;
    };

    static void deleteUser(int user_ID) {
    };
}