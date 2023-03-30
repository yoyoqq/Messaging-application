package src.Server.Test;

import org.junit.Before;
import org.junit.Test;

import src.Server.Database.DatabaseProxy;
import src.Server.ServerHandler.Coordinator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// This class, CoordinatorTest, is part of the src.Server.Database.Test package and is used to test the functionality of the Coordinator class, which is responsible for coordinating group chat activities.
public class CoordinatorTest {

    private Coordinator coordinator;
    private DatabaseProxy proxy;

    @Before
    public void setUp() {
        proxy = DatabaseProxy.getInstance();
        coordinator = new Coordinator(proxy);
    }

    @Test
    public void testLookForMembers() {
        String[] groupChatIds = { "1", "2", "3" };
        Map<String, ArrayList<String>> result = coordinator.lookForMembers(groupChatIds);
        System.out.println(result);
        // Define expected members for each group
        ArrayList<String> expectedGroup1Members = new ArrayList<>();
        ArrayList<String> expectedGroup2Members = new ArrayList<>();
        ArrayList<String> expectedGroup3Members = new ArrayList<>();

        // Add expected members to the lists (based on your test data)
        expectedGroup1Members.add("1");
        expectedGroup2Members.add("2");
        expectedGroup3Members.add("3");

        // Assert the size of the result map
        assertEquals(3, result.size());
    }

    @Test
    public void testSelectRandomCoordinator() {
        // Create test group data
        Map<String, ArrayList<String>> groupData = new HashMap<>();
        ArrayList<String> group1Members = new ArrayList<>();
        group1Members.add("1");
        group1Members.add("2");
        groupData.put("1", group1Members);

        ArrayList<String> group2Members = new ArrayList<>();
        group2Members.add("3");
        group2Members.add("4");
        groupData.put("2", group2Members);

        // Invoke the selectRandomCoordinator method
        coordinator.selectRandomCoordinator(groupData);

        // Get the updated coordinator for each group
        int newGroup1Coordinator = Integer.parseInt(proxy.getCoordinator(1));
        int newGroup2Coordinator = Integer.parseInt(proxy.getCoordinator(2));

        // Assert that the new coordinators are valid connected users
        assertTrue(group1Members.contains(String.valueOf(newGroup1Coordinator)));
        assertTrue(group2Members.contains(String.valueOf(newGroup2Coordinator)));
    }

}
