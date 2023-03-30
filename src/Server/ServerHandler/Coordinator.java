package src.Server.ServerHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import src.Server.Database.DatabaseProxy;

import java.util.Random;

/*
 * The class manages the coordinator feature. The coordinator is changed automaticaly if the user disconnects by looking all the groupchats.
 */
public class Coordinator {
    private Timer timer;
    private TimerTask task;
    private HashSet<Integer> connected_users = new HashSet<>(); // Observer pattern
    private DatabaseProxy proxy;
    private int TIMER = 2000; // run function every X seconds

    public Coordinator(DatabaseProxy proxy) {
        this.proxy = proxy;
        this.timer = new Timer();
        updateCoodinator(); // Call the method to create and assign a new TimerTask
        timer.schedule(task, 0, this.TIMER); // Pass the task object to the
        // timer.schedule(); // method
    }

    // run every "TIMER" seconds
    private void updateCoodinator() {
        this.task = new TimerTask() {
            @Override
            public void run() {
                // Code to be executed every "TIMER" seconds
                iterator();
            }
        };
    }

    // Chain of responsibility design pattern. It runs the algorihtm depending on
    // the current state of the users and groups.
    private void iterator() {
        // if none return, no users connected
        if (!getConnectedUsers())
            return;
        String[] groupChat_IDs = get_groups_from_not_active_coordinators();
        // if all the members are active
        if (groupChat_IDs.length == 0)
            return;
        Map<String, ArrayList<String>> groups_without_coordinator = lookForMembers(groupChat_IDs);
        selectRandomCoordinator(groups_without_coordinator);
    }

    // get the connected users from the ChatServer
    private boolean getConnectedUsers() {
        connected_users = new HashSet<>();
        for (UserThread user : ChatServer.userThreads) {
            connected_users.add(user.getID());
        }
        if (connected_users.size() == 0) {
            return false;
        }
        return true;
    }

    // get all the groupchats that dont have an active coordinator
    private String[] get_groups_from_not_active_coordinators() {
        String groups_from_not_active_coordinator = "";
        String groupChats = proxy.getGroupChats(); // string of groupChats
        String[] groups = groupChats.split("/");
        for (String group : groups) {
            String[] separated = group.split(":"); // group:coordinator
            String g = separated[0];
            int coordinator = Integer.parseInt(separated[1]);
            if (!connected_users.contains(coordinator)) {
                groups_from_not_active_coordinator += g + "/";
            }
        }
        String[] result = groups_from_not_active_coordinator.split("/");

        return result;
    }

    // for every groupchat that has no active coordinator, append it to the Map
    /*
     * @param gropuchat_ID's from non active coordinators
     */
    public Map<String, ArrayList<String>> lookForMembers(String[] groupChat_IDs) {
        HashMap<String, ArrayList<String>> groups_without_coordinator = new HashMap<>();
        // iterate through all the groupchats from non active coordinators
        for (String group : groupChat_IDs) {
            // get members of a group
            String[] groupUsers = proxy.getGroupUsers(Integer.parseInt(group)).split("/"); // 1/2/3 -> users_id
            // get the ones that are connected
            ArrayList<String> array = new ArrayList<>();
            for (String user : groupUsers) {
                if (this.connected_users.contains(Integer.parseInt(user))) {
                    array.add(user);
                }
            }
            // System.out.println(group + " " + array);
            groups_without_coordinator.put(group, array);
        }
        return groups_without_coordinator;
    }

    // select a random coordinator from all the inactive groups
    public void selectRandomCoordinator(Map<String, ArrayList<String>> groupData) {
        for (Entry<String, ArrayList<String>> entry : groupData.entrySet()) {
            int gropuchat_ID = Integer.parseInt(entry.getKey());
            ArrayList<String> values = entry.getValue();
            if (values.size() == 0) {
                continue;
            }
            // select random
            int user_id = Integer.parseInt(values.get(new Random().nextInt(values.size())));
            proxy.updateCoordinator(gropuchat_ID, user_id);
            System.out.println("Coordinator from group: " + gropuchat_ID + ", changed to user_id: " + user_id);
        }
    }

    public static void main(String[] args) {
        // DatabaseProxy proxy = DatabaseProxy.getInstance();
        // new Coordinator(proxy);
        // proxy.updateCoordinator(1, 1);
        // coordinator.iterator();
        // coordinator.getConnectedUsers();
        // String[] a = coordinator.get_groups_from_not_active_coordinators();
    }
}
