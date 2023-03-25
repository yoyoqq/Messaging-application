package src.Server.Database.Test;

import src.Server.Database.Data;
import src.Server.Database.Database;
import src.Server.Database.DatabaseProxy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.sql.*;
import java.util.Arrays;


public class DatabaseProxyTest {
    private DatabaseProxy proxy;

    /**
     * 
     */
    @Before
    public void setUp() {
        proxy = DatabaseProxy.getInstance();
    }

    @Test
    public void testGetInstance() {
        DatabaseProxy instance1 = DatabaseProxy.getInstance();
        DatabaseProxy instance2 = DatabaseProxy.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testPutUser() {
        String result1 = proxy.putUser("Alice", "127.0.0.1", 9000);
        assertNotNull(result1);

        String result2 = proxy.putUser("Alice", "127.0.0.1", 9000);
        assertEquals(result1, result2);

        String result3 = proxy.putUser("Bob", "127.0.0.1", 9001);
        assertNotNull(result3);
        assertNotEquals(result1, result3);
    }

    @Test
    public void testGetDatabase() {
        Database db1 = proxy.getDatabase();
        assertNotNull("Database instance should not be null.", db1);

        Database db2 = proxy.getDatabase();
        assertSame("Database instances should be the same.", db1, db2);
    }

    // Test the putGroupChat method.
    @Test
    public void testPutGroupChat() {
        // Get initial count of group chats.
        int initialGroupChatCount = countGroupChats();
        int coordinator = 1;

        // Call putGroupChat method.
        proxy.putGroupChat(coordinator);

        // Get new count of group chats.
        int newGroupChatCount = countGroupChats();
        // Check if the count increased by 1.
        assertEquals(initialGroupChatCount + 1, newGroupChatCount);
    }

    @Test
    public void testPutUserInGroups() {
        // Add a user to the database
        String userResponse = proxy.putUser("Alice", "10.0.0.1", 9000);
        int userId = Integer.parseInt(userResponse.split(" ")[0]);

        // Create a new group chat with the user as a coordinator
        String groupChatResponse = proxy.putGroupChat(userId);
        int groupChatId = Integer.parseInt(groupChatResponse.split(": ")[1]);

        // Add a new user to the database
        String newUserResponse = proxy.putUser("Bob", "10.0.0.2", 9001);
        int newUserId = Integer.parseInt(newUserResponse.split(" ")[0]);

        // Add the new user to the group chat created by the coordinator
        String putUserInGroupsResponse = proxy.putUserInGroups(newUserId, groupChatId, userId);

        // Expected result: the new user is added to the group chat
        String expectedResult = "User " + newUserId + " added to the groupchat " + groupChatId;
        assertEquals(expectedResult, putUserInGroupsResponse);

        // Verify that the new user is actually in the group chat
        String userInGroups = proxy.getUserInGroups(newUserId);
        assertEquals(String.valueOf(groupChatId), userInGroups);
    }


    @Test
    public void testPutMessage() {
        // Define sample input data
        int groupChat_ID = 1;
        int user_ID = 1;
        String dateTime = "10:30";
        String text = "Hello World";

        // Call the putMessage method with the sample input data
        String result = proxy.putMessage(groupChat_ID, user_ID, dateTime, text);

        assertEquals("", result);
    }

    @Test
    public void testGetUsers() {
        // Add a sample user to the database
        proxy.putUser("Alice", "10.0.0.1", 9876);

        // Call the getUsers method
        String result = proxy.getUsers();

        // Check if the result contains the expected user information
        assertTrue(result.contains("Alice"));
        assertTrue(result.contains("10.0.0.1"));
        assertTrue(result.contains("9876"));
    }

    @Test
    public void testGetGroupChats() {
        // Add a sample user to the database
        String userInfo = proxy.putUser("Bob", "10.0.0.2", 9876);

        // Parse the user ID from the userInfo string
        // Assuming userInfo is in the format: "user_ID userName ipAddress portNumber"
        String[] userInfoParts = userInfo.split(" ");
        int userId = Integer.parseInt(userInfoParts[0]);

        // Create a new group chat with the user as coordinator
        proxy.putGroupChat(userId);

        // Call the getGroupChats method
        String result = proxy.getGroupChats();

        // Check if the result contains the expected group chat information
        assertTrue(result.contains(String.valueOf(userId)));
    }

    @Test
    public void testGetCoordinator() {
        DatabaseProxy dbProxy = DatabaseProxy.getInstance();
        
        // Prepare test data
        // Assuming there is a group chat with ID 1 in the database
        int groupChatId = 1;

        // Call the method to test
        String coordinatorId = dbProxy.getCoordinator(groupChatId);

        // Check if the result is not null
        assertNotNull("Coordinator ID should not be null", coordinatorId);

        // Check if the result is an integer value
        try {
            int coordinator = Integer.parseInt(coordinatorId);
            assertTrue("Coordinator ID should be a valid integer", coordinator > 0);
        } catch (NumberFormatException e) {
            fail("Coordinator ID should be a valid integer");
        }
    }

    @Test
    public void testGetUserInGroups() {
        DatabaseProxy proxy = DatabaseProxy.getInstance();
        
        // Create a new user and add them to the database
        String userResponse = proxy.putUser("TestUser", "127.0.0.1", 12345);
        int userId = Integer.parseInt(userResponse.split(" ")[0]);
        
        // Create a new group chat and add the user to it
        proxy.putGroupChat(userId);
        
        // Get the user's groups
        String groups = proxy.getUserInGroups(userId);
        
        // Check if the groups string is not empty
        assertFalse("Groups should not be empty", groups.isEmpty());
        
        // Split the groups string and check if each group ID is a valid integer
        String[] groupIds = groups.split("/");
        for (String groupId : groupIds) {
            try {
                Integer.parseInt(groupId);
            } catch (NumberFormatException e) {
                fail("Group list should contain valid integer group IDs");
            }
        }
    }

    @Test
    public void testGetGroupUsers() {
        // Create users
        int userId1 = extractUserId(proxy.putUser("Alice", "127.0.0.1", 1234));

        // Create group chat
        proxy.putGroupChat(userId1);
        int groupChatId = getLastCreatedGroupChatId();

        // Test getGroupUsers
        String groupUsers = proxy.getGroupUsers(groupChatId);
        String[] userIds = groupUsers.split("/");

        // Check if all users are in the group
        assertEquals(1, userIds.length);
        assertTrue(Arrays.asList(userIds).contains(Integer.toString(userId1)));
    }

    @Test
    public void testGetName() {
        // Create sample users and add them to the database
        String[] userInfo1 = proxy.putUser("Alice", "192.168.1.1", 1234).split(" ");
        String[] userInfo2 = proxy.putUser("Bob", "192.168.1.2", 2345).split(" ");
        String[] userInfo3 = proxy.putUser("Charlie", "192.168.1.3", 3456).split(" ");

        int userId1 = Integer.parseInt(userInfo1[0]);
        int userId2 = Integer.parseInt(userInfo2[0]);
        int userId3 = Integer.parseInt(userInfo3[0]);

        // Test if the getName method returns the correct names for the given user IDs
        assertEquals("Alice", proxy.getName(userId1));
        assertEquals("Bob", proxy.getName(userId2));
        assertEquals("Charlie", proxy.getName(userId3));
    }

    @Test
    public void testGetMessage() {
        // Set up initial data
        String[] user1 = proxy.putUser("Alice", "192.168.1.2", 10000).split(" ");
        int userId1 = Integer.parseInt(user1[0]);

        proxy.putGroupChat(userId1); // Group chat ID should be 1

        String[] user2 = proxy.putUser("Bob", "192.168.1.3", 10001).split(" ");
        int userId2 = Integer.parseInt(user2[0]);

        // Create a new group chat with Alice as the coordinator
        String groupChatResponse = proxy.putGroupChat(userId1);
        int groupChatId = Integer.parseInt(groupChatResponse.split(": ")[1]);

        // Add Bob to the group chat
        proxy.putUserInGroups(userId2, groupChatId, userId1);

        // Send some messages
        proxy.putMessage(groupChatId, userId1, "10:30", "Hello, Bob!");
        proxy.putMessage(groupChatId, userId2, "10:31", "Hi Alice!");

        // Retrieve messages from the group chat for Alice
        String messagesForAlice = proxy.getMessage(groupChatId, userId1);
        System.out.println(messagesForAlice);
        // Verify that Alice can see the messages sent by both users
        assertTrue(messagesForAlice.contains("Alice    (10:30): Hello, Bob!                    (Message read by: Alice )(Message not read by: Bob ) "));
        assertTrue(messagesForAlice.contains("Bob      (10:31): Hi Alice!                      (Message read by: Bob Alice )(Message not read by: ) "));

        // Retrieve messages from the group chat for Bob
        String messagesForBob = proxy.getMessage(groupChatId, userId2);
        System.out.println(messagesForBob);
        // Verify that Bob can see the messages sent by both users
        assertTrue(messagesForBob.contains("Alice    (10:30): Hello, Bob!                    (Message read by: Alice Bob )(Message not read by: ) "));
        assertTrue(messagesForBob.contains("Bob      (10:31): Hi Alice!                      (Message read by: Bob Alice )(Message not read by: ) "));

    }
    

    @Test
    public void testGetMessageIDFromGroupchat() {
        // Set up initial data
        String user1Info = proxy.putUser("Alice", "192.168.0.2", 1234);
        int user1Id = Integer.parseInt(user1Info.split(" ")[0]);

        proxy.putGroupChat(user1Id);

        // Add messages to the group chat
        proxy.putMessage(1, user1Id, "10:30", "Hello, Bob!");

        // Call get_messageID_from_Groupchat and store the result
        String result = proxy.get_messageID_from_Groupchat(1);

        // Split the result into message IDs
        String[] messageIds = result.trim().split(" ");
        System.out.println(messageIds.length);
        // Verify the output using JUnit's assertions
        assertEquals("Expecting one message IDs", 1, messageIds.length);
    }

    @Test
    public void testGetMessageState() {
        // 1. Add a user to the database
        String user1Info = proxy.putUser("Alice", "192.168.0.2", 1234);
        int userId = Integer.parseInt(user1Info.split(" ")[0]);

        // 2. Add a group chat and store its ID
        int groupChatId = Integer.parseInt(proxy.putGroupChat(userId).split(": ")[1]);

        // 3. Add the user to the group chat
        proxy.putUserInGroups(userId, groupChatId, userId);

        // 4. Send a message to the group chat
        proxy.putMessage(groupChatId, userId, "10:30", "Hello, world!");

        // 5. Fetch the message state for the group chat
        String messageState = proxy.getMessageState(groupChatId);
        System.out.println(messageState);

        // 6. Call the getMessage method to mark the message as read
        proxy.getMessage(groupChatId, userId);

        // 7. Fetch the message state again after marking the message as read
        String updatedMessageState = proxy.getMessageState(groupChatId);

        // 8. Check if the updated message state no longer contains the user ID
        assertFalse("The updated message state should not contain the user ID", updatedMessageState.contains(String.valueOf(userId)));
    }

    @Test
    public void testDeleteUserFromGroupChat() {
        // 1. Add two users to the database
        String[] coordinatorInfo = proxy.putUser("Alice", "192.168.0.1", 12345).split(" ");
        String[] memberInfo = proxy.putUser("Bob", "192.168.0.2", 23456).split(" ");
        int coordinatorId = Integer.parseInt(coordinatorInfo[0]);
        int memberId = Integer.parseInt(memberInfo[0]);

        // 2. Add a group chat and store its ID
        int groupChatId = Integer.parseInt(proxy.putGroupChat(coordinatorId).split(": ")[1]);

        // 3. Add both users to the group chat
        proxy.putUserInGroups(coordinatorId, groupChatId, coordinatorId);
        proxy.putUserInGroups(memberId, groupChatId, coordinatorId);

        // 4. Check if the member is part of the group chat
        String groupUsersBeforeDeletion = proxy.getGroupUsers(groupChatId);
        assertTrue("The group should contain the member", groupUsersBeforeDeletion.contains(String.valueOf(memberId)));

        // 5. Delete the member from the group chat
        String deleteResult = proxy.deleteUserFromGroupChat(groupChatId, memberId, coordinatorId);
        assertEquals("The deletion result should be 'Successfully removed'", "Successfully removed", deleteResult);

        // 6. Check if the member is no longer part of the group chat
        String groupUsersAfterDeletion = proxy.getGroupUsers(groupChatId);
        assertFalse("The group should not contain the member", groupUsersAfterDeletion.contains(String.valueOf(memberId)));
    }

    // helper function
    private int countGroupChats() {
        // Count the number of group chats
        String groupChats = proxy.getGroupChats();
        if (groupChats.isEmpty()) {
            return 0;
        }
        return groupChats.split("/").length;
    }

    private int parseUserId(String userInfo) {
        // Parse the user ID from the user info string
        String[] parts = userInfo.split(" ");
        return Integer.parseInt(parts[0]);
    }

    private int getLastCreatedGroupChatId() {
        String lastCreatedGroupChat = null;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bin/src/Server/Database/Database.db");
                Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM GROUPCHAT ORDER BY GROUPCHAT_ID DESC LIMIT 1");
            if (rs.next()) {
                lastCreatedGroupChat = rs.getString("GROUPCHAT_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(lastCreatedGroupChat);
    }

    private int extractUserId(String userInfo) {
        String[] parts = userInfo.split(" ");
        return Integer.parseInt(parts[0]);
    }

}
