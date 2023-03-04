package Server.Database;

import java.util.ArrayList;

import User.ClientHandler.ChatClient;

// prototype pattern, create clones of user
class User {
    private static int id_counter = 1000;
    int id;
    private int port;
    private int ip;
    String name;

    // Cache
    ArrayList<GroupChat> chats;
    ChatClient chatClient;

    public User(String name) {
        this.name = name;
        chats = new ArrayList<>();
    }

    void setID() {
        id = id_counter;
        id_counter += 1;
    }

}
