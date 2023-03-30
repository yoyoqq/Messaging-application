package src.Server.Test;

import org.junit.Before;
import org.junit.Test;
import src.Server.Database.DatabaseProxy;
import src.Server.ServerHandler.API;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/*
 * APITest, contains a series of test methods to verify the functionality of the API class, which serves as an interface between the server and the database.
 */
public class APITest {
    private DatabaseProxy proxy;
    private API api;

    @Before
    public void setUp() {
        proxy = DatabaseProxy.getInstance();
    }

    @Test
    public void testGetMessageForInvalidInput() {
        String[] invalidMessage = { "", "invalid_input" };
        api = new API(invalidMessage, proxy, 1);
        String result = api.getMessage();
        assertEquals("Invalid input", result);
    }

    @Test
    public void testGetMessageForPutNewGroupChat() {
        String[] putNewGroupChat = { "", "put", "newGroupChat" };
        api = new API(putNewGroupChat, proxy, 1);
        String result = api.getMessage();
        System.out.println(result);
        // Assuming that the result will be in the format: "GroupChat_ID: <ID>"
        assertTrue(result.startsWith("GroupChat_ID: "));
    }

    @Test
    public void testGetMessageForPutUserInGroup_Coordinator() {
        // Set the coordinator for the group chat
        proxy.updateCoordinator(1, 1); // Set user with ID 1 as the coordinator for group chat with ID 1

        // Test case for successfully putting a user in a group when the user is a
        // coordinator
        String[] putUserInGroup = { "", "put", "userInGroup", "1", "2" };
        API api = new API(putUserInGroup, proxy, 1); // Assuming user with ID 1 is the coordinator for group chat with
                                                     // ID 1
        String result = api.getMessage();

        // Assuming the successful result will be in the format: "User <userID> added to
        // group <groupID>"
        String expectedMessage = "User 2 added to the groupchat 1";
        assertEquals(expectedMessage, result);
    }

    @Test
    public void testGetMessageForPutMessage() {
        // Test case for successfully putting a message in a group chat
        String[] putMessage = { "", "put", "message", "1", "Hello, everyone!" };
        API api = new API(putMessage, proxy, 1); // Assuming user with ID 1 is a member of group chat with ID 1
        String result = api.getMessage();

        // Assuming the successful result will be in the format: "Message sent to group
        // <groupID>"
        String expectedMessage = "";
        assertEquals(expectedMessage, result);
    }

    @Test
    public void testGetMessageForGetId() {
        // Test case for getting user ID
        int userId = 1;
        String[] getId = { "", "get", "id" };
        API api = new API(getId, proxy, userId);
        String result = api.getMessage();
        assertEquals("1", result);
    }

    @Test
    public void testGetMessageForGetUsers() {
        // Test case for getting all users
        int userId = 1;
        String[] getUsers = { "", "get", "users" };
        API api = new API(getUsers, proxy, userId);
        String result = api.getMessage();
        System.out.println(result);
        // Check if the result is not null.
        // Becasue it's hard to assert the exact result since it depends on the current
        // state of the database.
        assertNotNull(result);
    }

    @Test
    public void testGetMessageForGetConnectedUsers() {
        // Test case for getting connected users
        int userId = 1;
        String[] getConnectedUsers = { "", "get", "connectedUsers" };
        API api = new API(getConnectedUsers, proxy, userId);
        String result = api.getMessage();

        // Check if the result is not null.
        assertNotNull(result);
    }

    @Test
    public void testGetMessageForGetCoordinator() {
        // Set up a group chat and a user to use for testing
        int userId = 1;
        String[] createGroupChat = { "", "put", "newGroupChat" };
        API apiCreateGroup = new API(createGroupChat, proxy, userId);
        String groupChatIdResult = apiCreateGroup.getMessage();
        int groupChatId = Integer.parseInt(groupChatIdResult.substring("GroupChat_ID: ".length()).trim());

        // Test case for getting the coordinator of the group chat
        String[] getCoordinator = { "", "get", "coordinator", String.valueOf(groupChatId) };
        API apiGetCoordinator = new API(getCoordinator, proxy, userId);
        String result = apiGetCoordinator.getMessage();
        System.out.println(result);
        // Assert that the result is not null and the coordinator ID matches the user ID
        // (since the creator is the coordinator)
        assertNotNull(result);
        assertEquals(String.valueOf(userId), result);
    }

    @Test
    public void testGetMessageForGetUserInGroups() {
        // Set up a group chat and a user to use for testing
        int userId = 1;
        String[] createGroupChat = { "", "put", "newGroupChat" };
        API apiCreateGroup = new API(createGroupChat, proxy, userId);
        String groupChatIdResult = apiCreateGroup.getMessage();
        int groupChatId = Integer.parseInt(groupChatIdResult.substring("GroupChat_ID: ".length()).trim());

        // Test case for getting the user's groups
        String[] getUserInGroups = { "", "get", "userInGroups", String.valueOf(userId) };
        API apiGetUserInGroups = new API(getUserInGroups, proxy, userId);
        String result = apiGetUserInGroups.getMessage();
        System.out.println(result);

        // Assert that the result is not null and contains the created group chat ID
        assertNotNull(result);
        assertTrue(result.contains(String.valueOf(groupChatId)));
    }

    @Test
    public void testGetMessageForGetGroupMembers() {
        // Set up a group chat and a user to use for testing
        int userId = 1;
        String[] createGroupChat = { "", "put", "newGroupChat" };
        API apiCreateGroup = new API(createGroupChat, proxy, userId);
        String groupChatIdResult = apiCreateGroup.getMessage();
        int groupChatId = Integer.parseInt(groupChatIdResult.substring("GroupChat_ID: ".length()).trim());

        // Test case for getting the group members
        String[] getGroupMembers = { "", "get", "groupMembers", String.valueOf(groupChatId) };
        API apiGetGroupMembers = new API(getGroupMembers, proxy, userId);
        String result = apiGetGroupMembers.getMessage();

        // Assert that the result is not null and contains the user ID
        assertNotNull(result);
        assertTrue(result.contains(String.valueOf(userId)));
    }

    @Test
    public void testGetMessageForGetName() {
        // Assuming that user with ID 1 exists in the database and has a name
        int userId = 1;

        // Test case for getting the name of the user
        String[] getName = { "", "get", "name", String.valueOf(userId) };
        API apiGetName = new API(getName, proxy, userId);
        String result = apiGetName.getMessage();
        System.out.println(result);

        // Assert that the result is not null and is not an error message
        assertNotNull(result);
        assertFalse(result.startsWith("400"));
        assertTrue(result.equals("alice"));
    }

    @Test
    public void testGetMessageForGetMessages() {
        // Assuming that user with ID 1 exists in the database
        // and is a member of a group chat with ID 1
        int userId = 1;
        int groupChatId = 1;

        // Test case for getting messages from a group chat
        String[] getMessages = { "", "get", "messages", String.valueOf(groupChatId) };
        API apiGetMessages = new API(getMessages, proxy, userId);
        String result = apiGetMessages.getMessage();
        System.out.println(result);

        // Assert that the result is not null and is not an error message
        assertNotNull(result);
        assertFalse(result.startsWith("400"));
    }

    @Test
    public void testGetMessageForKickUser() {
        // Assuming that user with ID 1 exists in the database
        // and is a coordinator of a group chat with ID 1
        int coordinatorUserId = 1;
        int groupChatId = 1;

        // Assuming that user with ID 2 exists in the database
        // and is a member of the group chat with ID 1
        int userToKickId = 2;

        // Test case for kicking a user from a group chat
        String[] kickUser = { "", "kickUser", String.valueOf(groupChatId), String.valueOf(userToKickId) };
        API apiKickUser = new API(kickUser, proxy, coordinatorUserId);
        String result = apiKickUser.getMessage();

        // Assert that the result indicates the user has been kicked successfully
        assertEquals("Successfully removed", result);
    }
}
