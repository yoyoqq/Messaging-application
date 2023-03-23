package Server.Database;

import java.io.File;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// https://www.youtube.com/watch?v=0beocykXUag
public class Database implements Data {
    static String className = "org.sqlite.JDBC";
    static String url = "jdbc:sqlite:Server/Database/Database.db";
    static String path = "Server/Database/Database.db";
    static Connection c = null;
    static Statement stmt = null;

    public Database() {
        // createDatabase();
        // createData(); // mock data
    }

    static void deleteDatabse() {
        try {
            File dbFile = new File(path);
            dbFile.delete();
        } catch (Exception e) {
            System.out.println("Exception :" + e);
        }
    }

    static void createUserTable() {
        try {
            Class.forName(className);
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE USER " +
                    "(USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " NAME TEXT, " +
                    " IP TEXT, " +
                    " PORT INT)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    static void createGroupChatTable() {
        try {
            Class.forName(className);
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE GROUPCHAT " +
                    "(GROUPCHAT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " COORDINATOR INTEGER, " +
                    " FOREIGN KEY (COORDINATOR) REFERENCES USER(USER_ID))";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    static void createUserInGroupsTable() {
        try {
            Class.forName(className);
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE USERINGROUPS " +
                    "(USERINGROUPS_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " USER_ID INTEGER, " +
                    " GROUPCHAT_ID INTEGER, " +
                    " FOREIGN KEY (USER_ID) REFERENCES USER(USER_ID), " +
                    " FOREIGN KEY (GROUPCHAT_ID) REFERENCES GROUPCHAT(GROUPCHAT_ID))";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    static void createMessagesTable() {
        try {
            Class.forName(className);
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE MESSAGES " +
                    "( MESSAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " USER_ID INTEGER, " +
                    " GROUPCHAT_ID INTEGER, " +
                    " TIME TEXT, " +
                    " MESSAGE TEXT, " +
                    " FOREIGN KEY (USER_ID) REFERENCES USER(USER_ID), " +
                    " FOREIGN KEY (GROUPCHAT_ID) REFERENCES GROUPCHAT(GROUPCHAT_ID))";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    static void createMessageStateTable() {
        try {
            Class.forName(className);
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            String sql = "CREATE TABLE MESSAGESTATE " +
                    "( MESSAGEDATE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " USER_ID INTEGER, " +
                    " MESSAGE_ID INTEGER, " +
                    " GROUPCHAT_ID INTEGER, " +
                    " MESSAGE_STATE BOOLEAN DEFAULT false, " +
                    " FOREIGN KEY (USER_ID) REFERENCES USER(USER_ID), " +
                    " FOREIGN KEY (GROUPCHAT_ID) REFERENCES GROUPCHAT(GROUPCHAT_ID), " +
                    " FOREIGN KEY (MESSAGE_ID) REFERENCES MESSAGE(MESSAGE_ID))";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    static void createDatabase() {
        // delete db if exists
        deleteDatabse();

        // create User table
        createUserTable();

        // create groupChat table
        createGroupChatTable();

        // create userInGroups
        createUserInGroupsTable();

        // create messages table
        createMessagesTable();

        // create message state
        createMessageStateTable();
    }

    public String putUser(String name, String ip, Integer port) {
        // error handler
        if (name.equals("Server") || ip.equals("0") || port.equals(0)) {
            return "null";
        }

        int last_generated_key = -1;
        try {
            Class.forName(className);
            c = DriverManager.getConnection(url);
            c.setAutoCommit(false);
            try (Connection conn = DriverManager.getConnection(url);
                    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO USER (name,ip,port) VALUES(?,?,?)",
                            PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, name);
                pstmt.setString(2, ip);
                pstmt.setInt(3, port);
                pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    last_generated_key = rs.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            c.commit();
            c.close();
            return last_generated_key + " " + name + " " + ip + " " + port;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            return null;
        }
    }

    public void putGroupChat(int coordinator) {
        try {
            int last_generated_key = -1;
            Class.forName(className);
            c = DriverManager.getConnection(url);
            c.setAutoCommit(false);

            try (Connection conn = DriverManager.getConnection(url);
                    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO GROUPCHAT (COORDINATOR) VALUES(?)",
                            PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, coordinator);
                pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    last_generated_key = rs.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            c.commit();
            c.close();
            // add user to the groupchat
            putUserInGroups(coordinator, last_generated_key);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    void putUserInGroups(int user_ID, int groupChat_ID) {
        try {
            Class.forName(className);
            c = DriverManager.getConnection(url);
            c.setAutoCommit(false);

            try (Connection conn = DriverManager.getConnection(url);
                    PreparedStatement pstmt = conn
                            .prepareStatement("INSERT INTO USERINGROUPS (USER_ID, GROUPCHAT_ID) VALUES(?,?)")) {
                pstmt.setInt(1, user_ID);
                pstmt.setInt(2, groupChat_ID);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // uses putMessageState()
    boolean putMessage(int groupChat_ID, int user_ID, String dateTime, String text) {
        int last_generated_key = -1;
        // add message to database
        try {
            Class.forName(className);
            c = DriverManager.getConnection(url);
            c.setAutoCommit(false);

            try (Connection conn = DriverManager.getConnection(url);
                    PreparedStatement pstmt = conn
                            .prepareStatement(
                                    // "INSERT INTO MESSAGES (USER_ID, GROUPCHAT_ID, TIME, MESSAGE)
                                    // VALUES(?,?,?,?)")) {
                                    "INSERT INTO MESSAGES (USER_ID, GROUPCHAT_ID, TIME, MESSAGE) VALUES(?,?,?,?)",
                                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, user_ID);
                pstmt.setInt(2, groupChat_ID);
                pstmt.setString(3, dateTime);
                pstmt.setString(4, text);
                pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    last_generated_key = rs.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        // put own mark as read
        putMessageState(user_ID, last_generated_key, groupChat_ID, true);

        // notify server to create MessageState for each user in that groupchat
        // get all the users that are in gropuChatId
        String[] users = getGroupUsers(groupChat_ID).split("/");
        for (String user : users) {
            if (Integer.parseInt(user) == user_ID)
                continue;
            putMessageState(Integer.parseInt(user), last_generated_key, groupChat_ID, false);
        }
        return true;
    }

    // for each message create a state for all the users that have or not read the
    // message
    private void putMessageState(int user_ID, int message_ID, int groupChat_ID, boolean bool) {
        try {
            Class.forName(className);
            c = DriverManager.getConnection(url);
            c.setAutoCommit(false);

            try (Connection conn = DriverManager.getConnection(url);
                    PreparedStatement pstmt = conn
                            .prepareStatement(
                                    "INSERT INTO MESSAGESTATE (USER_ID, MESSAGE_ID,GROUPCHAT_ID,MESSAGE_STATE) VALUES(?,?,?,?)")) {
                pstmt.setInt(1, user_ID);
                pstmt.setInt(2, message_ID);
                pstmt.setInt(3, groupChat_ID);
                pstmt.setBoolean(4, bool);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    static void createData(Database db) {
        // create user
        db.putUser("alice", "192.168.0.1", 5678);
        db.putUser("bob", "10.0.0.1", 9876);
        db.putUser("charlie", "172.16.0.1", 4321);
        db.putUser("dave", "192.168.1.1", 5555);
        db.putUser("eve", "192.168.2.1", 4444);
        db.putUser("frank", "192.168.3.1", 7777);
        db.putUser("grace", "192.168.4.1", 8888);
        db.putUser("harry", "192.168.5.1", 9999);
        db.putUser("ian", "192.168.6.1", 2222);
        db.putUser("jane", "192.168.7.1", 3333);

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

    public String getUsers() {
        String users = "";
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);

            // Create a statement object
            Statement stmt = conn.createStatement();

            // Execute the query to get all data from the user table
            ResultSet rs = stmt.executeQuery("SELECT * FROM user");

            // Loop through the result set and create User objects
            while (rs.next()) {
                int userId = rs.getInt("USER_ID");
                String name = rs.getString("NAME");
                String ip = rs.getString("IP");
                int port = rs.getInt("PORT");

                // formatter
                if (users.length() == 0) {
                    users += userId + " " + name + " " + ip + " " + port;
                } else {
                    users += "/" + userId + " " + name + " " + ip + " " + port;
                }
            }
            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return users;
    }

    String getGroupChats() {
        String groupChats = "";
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);

            // Create a statement object
            Statement stmt = conn.createStatement();

            // Execute the query to get all data from the user table
            ResultSet rs = stmt.executeQuery("SELECT * FROM GROUPCHAT");

            // Loop through the result set and create User objects
            while (rs.next()) {
                int groupchat_id = rs.getInt("GROUPCHAT_ID");
                int coordinator = rs.getInt("COORDINATOR");

                // formatter
                if (groupChats.length() == 0) {
                    groupChats += groupchat_id + ":" + coordinator;
                } else {
                    groupChats += "/" + groupchat_id + ":" + coordinator;
                }
            }
            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return groupChats;
    }

    String getCoordinator(int groupChat_ID) {
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            String sql = "SELECT COORDINATOR FROM GROUPCHAT WHERE GROUPCHAT_ID = " + groupChat_ID;
            ResultSet rs = stmt.executeQuery(sql);

            // check if the result set is not empty
            if (rs.next()) {
                int coordinatorId = rs.getInt("COORDINATOR");
                return Integer.toString(coordinatorId);
                // do something with the coordinator id
            }
            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    String getUserInGroups(int user_ID) {
        String groups = "";
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            String sql = "SELECT GROUPCHAT_ID FROM USERINGROUPS WHERE user_id = " + user_ID;
            ResultSet rs = stmt.executeQuery(sql);

            // check if the result set is not empty
            while (rs.next()) {
                int coordinatorId = rs.getInt("GROUPCHAT_ID");

                if (groups.length() == 0) {
                    groups += coordinatorId;
                } else {
                    groups += "/" + coordinatorId;
                }
            }
            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
            return groups;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    String getGroupUsers(int groupChat_ID) {
        String groups = "";
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            String sql = "SELECT USER_ID FROM USERINGROUPS WHERE GROUPCHAT_ID = " + groupChat_ID;
            ResultSet rs = stmt.executeQuery(sql);

            // check if the result set is not empty
            while (rs.next()) {
                int coordinatorId = rs.getInt("USER_ID");

                if (groups.length() == 0) {
                    groups += coordinatorId;
                } else {
                    groups += "/" + coordinatorId;
                }
            }
            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
            return groups;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    String getName(int user_ID) {
        String name = "";
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            String sql = "SELECT NAME FROM USER WHERE USER_ID = " + user_ID;
            ResultSet rs = stmt.executeQuery(sql);

            // check if the result set is not empty
            while (rs.next()) {
                name = rs.getString("NAME");
            }
            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
            return name;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    // uses getName function, try to get without static
    // @return get messages from groupchat
    String getMessage(int groupChat_ID) {
        String messages = "";
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            String sql = "SELECT GROUPCHAT_ID, USER_ID, TIME, MESSAGE FROM MESSAGES WHERE GROUPCHAT_ID = "
                    + groupChat_ID;
            ResultSet rs = stmt.executeQuery(sql);

            // check if the result set is not empty
            while (rs.next()) {
                int groupId = rs.getInt("GROUPCHAT_ID");
                int userId = rs.getInt("USER_ID");
                String name = getName(userId);
                String time = rs.getString("TIME");
                String message = rs.getString("MESSAGE");

                //
                if (messages.length() == 0) {
                    messages += groupId + " " + name + " " + time + " " + message;
                } else {
                    messages += "/" + groupId + " " + name + " " + time + " " + message;
                }
            }

            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
            return messages;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return "null";
    }

    String get_messageID_from_Groupchat(int groupChat_ID) {
        String messages = "";
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            String sql = "SELECT MESSAGE_ID FROM MESSAGES WHERE GROUPCHAT_ID = "
                    + groupChat_ID;
            ResultSet rs = stmt.executeQuery(sql);

            // check if the result set is not empty
            while (rs.next()) {
                int messageID = rs.getInt("MESSAGE_ID");

                //
                if (messages.length() == 0) {
                    messages += messageID;
                } else {
                    messages += "/" + messageID;
                }
            }

            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
            return messages;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return "null";
    }

    /**
     * @param groupChat_ID
     * @return
     */
    String getMessageState(int groupChat_ID) {
        // get all the messages ID
        // String messageState = "";

        // get message ID
        // String[] message_id = get_messageID_from_Groupchat(groupChat_ID).split("/ ");
        // return whos in the group (ID)
        // String[] user_in_group = getGroupUsers(groupChat_ID).split("/ ");

        String messages = "";
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            // String sql = "SELECT MESSAGE_ID FROM MESSAGES WHERE GROUPCHAT_ID = "
            String sql = "SELECT * FROM MESSAGESTATE WHERE GROUPCHAT_ID = " + groupChat_ID;
            ResultSet rs = stmt.executeQuery(sql);

            // check if the result set is not empty
            while (rs.next()) {
                int messageID = rs.getInt("MESSAGE_ID");
                int user_ID = rs.getInt("USER_ID");
                // int groupChat = rs.getInt("GROUPCHAT_ID");
                boolean message_state = rs.getBoolean("MESSAGE_STATE");

                // add to result
                // System.out.println(messageID + " " + getName(user_ID) + " " + message_state);
                String data = messageID + " " + getName(user_ID) + " " + message_state;
                if (messages.length() == 0) {
                    messages += data;
                } else {
                    messages += "/" + data;
                }
            }

            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
            return messages;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return "null";
    }

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

    private static void updateDeleteUser(int user_ID) {
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            stmt.executeUpdate("DELETE FROM USER where USER_ID = " + user_ID);

            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private static void updateDeleteUserInGroups(int user_ID) {
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            stmt.executeUpdate("DELETE FROM USERINGROUPS where USER_ID = " + user_ID);

            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private static void updateDeleteMessageState(int user_ID) {
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            stmt.executeUpdate("DELETE FROM MESSAGESTATE where USER_ID = " + user_ID);

            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // send data to all members that the user is in
    private static void putNotifyLeaving(int user_ID, String message) {
        // get the groupchats
        List<Integer> gropuChatsIn = new ArrayList<>();
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            String sql = "SELECT GROUPCHAT_ID FROM USERINGROUPS WHERE USER_ID = " + user_ID;
            ResultSet rs = stmt.executeQuery(sql);

            // check if the result set is not empty
            while (rs.next()) {
                int groupChatId = rs.getInt("GROUPCHAT_ID");
                gropuChatsIn.add(groupChatId);
            }
            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        // send message
        for (int groupChatId : gropuChatsIn) {
            try {
                Class.forName(className);
                Connection c = DriverManager.getConnection(url);
                c.setAutoCommit(false);

                try (PreparedStatement pstmt = c.prepareStatement(
                        "INSERT INTO MESSAGES (USER_ID, GROUPCHAT_ID, TIME, MESSAGE) VALUES(?,?,?,?)")) {
                    pstmt.setInt(1, user_ID);
                    pstmt.setInt(2, groupChatId);
                    pstmt.setString(3, LocalTime.now().toString());
                    pstmt.setString(4, message);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                c.commit();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
    }

    void deleteUser(int user_ID, String message) {
        putNotifyLeaving(user_ID, message);
        updateDeleteUser(user_ID);
        updateDeleteUserInGroups(user_ID);
        updateDeleteMessageState(user_ID);
    }

    String updadateCoordinator(int groupchat_id, int coordinator) {
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            String command = "UPDATE GROUPCHAT SET COORDINATOR = " + coordinator + " WHERE GROUPCHAT_ID = "
                    + groupchat_id;
            stmt.executeUpdate(command);
            stmt.close();
            conn.close();
            return "Groupchat: " + groupchat_id + " ,coordinator changed to: " + coordinator;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            return "null";
        }
    }

    public static void main(String[] args) {
        Database db = new Database();
        // String a = db.updadateCoordinator(13, 123);
        // createDatabase();
        // createData(db);
        // getData(db);

        // deleteUser(1, "user 1 leaving the chat");

        // String a = putUser("yagol", "12341234", 1234);
        // System.out.println(a);
        // db.putGroupChat(123);
    }
}
