package src.Runner;

import src.ClientServer.ChatClient;

public class RunUser {
    static String name;
    static String hostname;
    static int port;

    public static void main(String[] args) {

        // user1();
        // user2();
        // user3();
        // user4();
        // user5();
        user6();
    }

    static void user1() {
        name = "Oliver";
        hostname = "127.16.0.1";
        port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }

    static void user2() {
        name = "Emma";
        hostname = "127.16.0.2";
        port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }

    static void user3() {
        name = "James";
        hostname = "127.16.0.3";
        port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }

    static void user4() {
        name = "Mary";
        hostname = "127.16.0.4";
        port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }

    static void user5() {
        name = "Patricia";
        hostname = "127.16.0.5";
        port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }

    static void user6() {
        name = "bob";
        hostname = "127.16.0.5";
        port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }
}
