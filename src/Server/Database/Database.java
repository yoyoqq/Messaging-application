package src.Server.Database;

import java.io.File;
import java.sql.*;

// This class creates the database. It is also capable of creating simple Database queries.
// Refernce: https://www.youtube.com/watch?v=0beocykXUag
public class Database implements Data {
    static String className = "org.sqlite.JDBC";
    static String url = "jdbc:sqlite:src/Server/Database/Database.db";
    static String path = "src/Server/Database/Database.db";
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

    // look if user exists in database
    String findUser(String name, String ip, Integer port) {
        int user_id = -1;
        try {
            Class.forName(className);
            c = DriverManager.getConnection(url);
            String command = "SELECT USER_ID FROM USER WHERE NAME = ? AND IP = ? AND PORT = ? LIMIT 1";
            c.setAutoCommit(false);
            try (PreparedStatement pstmt = c.prepareStatement(command)) {
                pstmt.setString(1, name);
                pstmt.setString(2, ip);
                pstmt.setInt(3, port);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    user_id = rs.getInt("USER_ID");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            c.commit();
            c.close();
            if (Integer.toString(user_id).equals("-1")) {
                return "-1";
            }
            return Integer.toString(user_id) + " " + name + " " + ip + " " + port;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            return "-1";
        }
    }

    public String putGroupChat(int coordinator) {
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
            return Integer.toString(last_generated_key);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            return "Error in putGroupChat";
        }
    }

    public void putUserInGroups(int user_ID, int groupChat_ID) {
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
    public boolean putMessage(int groupChat_ID, int user_ID, String dateTime, String text) {
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
                String format = String.format("%-3d %-5s \t %s \t %d\n", userId, name, ip, port);
                // users += userId + " " + name + " " + ip + " " + port + "\n";
                users += format;

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

    public String getCoordinator(int groupChat_ID) {
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

    public String getUserInGroups(int user_ID) {
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

    public String getGroupUsers(int groupChat_ID) {
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
    public String getMessage(int groupChat_ID) {
        String messages = "";
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            String sql = "SELECT MESSAGE_ID, USER_ID, TIME, MESSAGE FROM MESSAGES WHERE GROUPCHAT_ID = "
                    + groupChat_ID;
            ResultSet rs = stmt.executeQuery(sql);

            // check if the result set is not empty
            while (rs.next()) {
                int messageID = rs.getInt("MESSAGE_ID");
                int userId = rs.getInt("USER_ID");
                String name = getName(userId);
                String time = rs.getString("TIME");
                String message = rs.getString("MESSAGE");

                //
                if (messages.length() == 0) {
                    messages += messageID + " " + name + " " + time + " " + message;
                } else {
                    messages += "/" + messageID + " " + name + " " + time + " " + message;
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
    public String getMessageState(int groupChat_ID) {
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

    /*
     * @param data of the user and gropuchat
     * 
     * @return if user exists in the group
     */
    boolean ifUserInGroup(int userID, int groupchat_id) {
        boolean inGroup = false;
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to count the number of rows where USER_ID = userID and
            // GROUPCHAT_ID = groupchat_id
            String sql = "SELECT COUNT(1) FROM USERINGROUPS WHERE USER_ID = " + userID + " AND GROUPCHAT_ID = "
                    + groupchat_id;
            ResultSet rs = stmt.executeQuery(sql);
            // Get the count from the result set
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            // If the count is greater than 0, then the user is in the group
            if (count > 0) {
                inGroup = true;
            }
            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return inGroup;
    }

    boolean ifUserIsCoordinator(int groupchat_id, int userID) {
        boolean inGroup = false;
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to count the number of rows where USER_ID = userID and
            // GROUPCHAT_ID = groupchat_id
            String sql = "SELECT COUNT(1) FROM GROUPCHAT WHERE GROUPCHAT_ID = " + groupchat_id + " AND COORDINATOR = "
                    + userID;
            ResultSet rs = stmt.executeQuery(sql);
            // Get the count from the result set
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            // If the count is greater than 0, then the user is in the group
            if (count > 0) {
                inGroup = true;
            }
            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return inGroup;
    }

    /*
     * @param remove user from groupchat
     */
    void deleteUserInGroup(int user_ID, int groupchat_ID) {
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            stmt.executeUpdate(
                    "DELETE FROM USERINGROUPS where USER_ID = " + user_ID + " AND GROUPCHAT_ID = " + groupchat_ID);
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
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

    void updateMessageState(int groupchat_id, int user_ID) {
        // for all the messages mark as read
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(url);
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute the query to get all data from the user table
            String command = "UPDATE MESSAGESTATE SET MESSAGE_STATE = " + true + " WHERE GROUPCHAT_ID = " + groupchat_id
                    + " AND USER_ID = " + user_ID;
            stmt.executeUpdate(command);
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        // Database db = new Database();
        // createDatabase();
    }
}
