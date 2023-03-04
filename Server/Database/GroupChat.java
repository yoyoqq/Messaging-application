package Server.Database;

/*
 * users
 * messages
 */
import java.util.ArrayList;
import java.util.HashMap;

public class GroupChat {
    // Integer -> is the id of the groupchat
    // whos in the groupchat
    HashMap<Integer, ArrayList<User>> users;
    // messages in the groupchat
    HashMap<Integer, ArrayList<Message>> groupChats;

    public GroupChat() {
        groupChats = new HashMap<>();
        users = new HashMap<>();
    }

    HashMap<Integer, ArrayList<Message>> get_group_chats() {
        return groupChats;
    }

    // groupchats
    void sendMessage(GroupChat groupChat, String message) {
    }

    void addUser(GroupChat groupChat, User user) {
    }
}
