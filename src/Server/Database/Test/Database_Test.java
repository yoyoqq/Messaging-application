package src.Server.Database.Test;

import src.Server.Database.Database;

public class Database_Test {
    static void getData(Database db) {
        // get data from database
        db.getUsers();

        // get whos the coordinator, from a specific groupchat
        String a;
        a = db.getCoordinator(5);
        System.out.println(a);

        // get user in group
        a = db.getUserInGroups(1); // return 1 and 4 -> user id 1
        System.out.println(a);
        String b = db.getUserInGroups(2); // return 2 and 1 -> user id 2
        System.out.println(b);

        // get messages from grouchat
        a = db.getMessage(1);
        System.out.println(a);
        b = db.getMessage(2);
        System.out.println(b);

        // GET GROUP USERS
        a = db.getGroupUsers(1);
        System.out.println(a);

        // get message state
        a = db.getMessageState(1);
        System.out.println(a);
    }

    static void createData(Database db) {
        // create user
        db.putUser("alice", "127.16.0.5", 1234);
        db.putUser("bob", "127.16.0.5", 1234);
        db.putUser("charlie", "127.16.0.5", 1234);
        db.putUser("dave", "127.16.0.5", 1234);
        db.putUser("eve", "127.16.0.5", 1234);
        db.putUser("frank", "127.16.0.5", 1234);
        db.putUser("grace", "127.16.0.5", 1234);
        db.putUser("harry", "127.16.0.5", 1234);
        db.putUser("ian", "127.16.0.5", 1234);
        db.putUser("jane", "127.16.0.5", 1234);

        // create groupchat
        db.putGroupChat(1);
        db.putGroupChat(2);
        db.putGroupChat(3);
        db.putGroupChat(4);
        db.putGroupChat(5);
        db.putGroupChat(6);
        db.putGroupChat(7);
        db.putGroupChat(8);
        db.putGroupChat(9);
        db.putGroupChat(10);
        db.putGroupChat(11);
        db.putGroupChat(12);

        // create user in groups
        db.putUserInGroups(1, 4);
        db.putUserInGroups(2, 2);
        db.putUserInGroups(1, 1);
        db.putUserInGroups(2, 1);
        db.putUserInGroups(3, 1);
        db.putUserInGroups(4, 1);
        db.putUserInGroups(3, 2);
        db.putUserInGroups(2, 1);
        db.putUserInGroups(1, 1);
        db.putUserInGroups(6, 1);

        // messages
        db.putMessage(1, 2, "12:20", "hi");
        db.putMessage(1, 3, "13:20", "hello");
        db.putMessage(1, 4, "14:20", "Im user 4");
        db.putMessage(1, 5, "15:20", "im user 5");
        db.putMessage(1, 6, "16:20", "im ujser 6");
        db.putMessage(1, 6, "18:20", "bye");
        db.putMessage(2, 6, "18:20", "bye");
        db.putMessage(2, 6, "18:20", "bye");

        // message state
        // putMessageState(1, 2);
        // putMessageState(2, 2);
        // putMessageState(3, 1);
        // putMessageState(4, 2);
        // putMessageState(5, 3);
    }

    public static void main(String[] args) {
        // Database db = new Database();
        // Database_Test db_test = new Database_Test();
        // db_test.createData(db);
    }
}