package src.Runner;

import src.ClientServer.ChatClient;

public class RunUser {
    public static void main(String[] args) {

        // user1();
        user2();
        // user3();
    }

    static void user1() {
        String name = "Yagol";
        String hostname = "127.16.0.2";
        int port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }

    static void user2() {
        String name = "Riya";
        String hostname = "127.16.0.1";
        int port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }

    static void user3() {
        String name = "Peter";
        String hostname = "127.16.0.3";
        int port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }
}
