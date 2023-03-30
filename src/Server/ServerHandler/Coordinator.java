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
 * when someone sends a message append to connected_users
 */

public class Coordinator {
    private Timer timer;
    private TimerTask task;
    private HashSet<Integer> connected_users = new HashSet<>();
    private DatabaseProxy proxy;
    private int TIMER = 2000;

    public Coordinator(DatabaseProxy proxy) {
        this.proxy = proxy;
        // dummy_data();
        this.timer = new Timer();
        updateCoodinator(); // Call the method to create and assign a new TimerTask
        timer.schedule(task, 0, this.TIMER); // Pass the task object to the
        // timer.schedule(); // method
    }

    private void updateCoodinator() {
        this.task = new TimerTask() {
            @Override
            public void run() {
                // Code to be executed every 2 seconds
                // System.out.println(connected_users);
                iterator();
            }
        };
    }

    /**
     * 
     */
    private void iterator() {
        // if none return
        if (!getConnectedUsers())
            return;
        String[] groupChat_IDs = get_groups_from_not_active_coordinators();
        if (groupChat_IDs.length == 0)
            return;
        Map<String, ArrayList<String>> groups_without_coordinator = lookForMembers(groupChat_IDs);
        selectRandomCoordinator(groups_without_coordinator);
    }

    // get the connected users from the ChatServer
    private boolean getConnectedUsers() {
        // check every "TIMER" seconds
        connected_users = new HashSet<>();
        for (UserThread user : ChatServer.userThreads) {
            connected_users.add(user.getID());
        }
        // System.out.println(connected_users);
        if (connected_users.size() == 0) {
            return false;
        }
        return true;
    }

    // get all the groupchats that are not in connected_users
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

    // is connected
    // for every groupchat that has no active coordinator assing a new one that it
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

    public void selectRandomCoordinator(Map<String, ArrayList<String>> groupData) {
        // System.out.println(groupData.);
        for (Entry<String, ArrayList<String>> entry : groupData.entrySet()) {
            // System.out.println(data);
            int gropuchat_ID = Integer.parseInt(entry.getKey());
            ArrayList<String> values = entry.getValue();
            // System.out.println(gropuchat_ID + " " + values);
            if (values.size() == 0) {
                // System.out.println("Group " + gropuchat_ID + " does not have any members
                // active");
                continue;
            }
            // select random
            // Random random = new Random();
            int user_id = Integer.parseInt(values.get(new Random().nextInt(values.size())));
            // System.out.println(values.get(randomIndex));
            proxy.updateCoordinator(gropuchat_ID, user_id);
            System.out.println("Coordinator from group: " + gropuchat_ID + ", changed to user_id: " + user_id);
            // System.out.println(result);
        }
    }

    // get all users id
    // get groupchat_id: [connected_users]
    void dummy_data() {
        // this.connected_users.add(1);
        // this.connected_users.add(2);
        // this.connected_users.add(4);
        // this.connected_users.add(3);
        // this.connected_users.add(6);
        // this.connected_users.add(7);
        // this.connected_users.add(8);
        // this.connected_users.add(9);
        // this.connected_users.add(10);
        // this.connected_users.add(11);
        // this.connected_users.add(123);
        // this.connected_users.add(12);
    }

    public static void main(String[] args) {
        DatabaseProxy proxy = DatabaseProxy.getInstance();
        new Coordinator(proxy);

        // proxy.updateCoordinator(1, 1);
        // coordinator.iterator();
        // coordinator.getConnectedUsers();

        // String[] a = coordinator.get_groups_from_not_active_coordinators();
    }
}
