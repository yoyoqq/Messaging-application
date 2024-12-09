package src.Runner;

import src.ClientServer.ChatClient;

// This class runs the user by specifying the name, IP address and port.
public class RunUser {
    static String name;
    static String hostname;
    static int port;

    public static void main(String[] args) {

        // user1();
        // user2();
        // user3();
        // user4();
        user5();
        // new_user6();
    }

    static void user1() {
        name = "Alice";
        hostname = "127.16.0.1";
        port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }

    static void user2() {
        name = "Bob";
        hostname = "127.16.0.32";
        port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }

    static void user3() {
        name = "Charlie";
        hostname = "127.16.0.3";
        port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }

    static void user4() {
        name = "Dave";
        hostname = "127.16.0.78";
        port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }

    static void user5() {
        name = "Eve";
        hostname = "127.16.0.23";
        port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }

    static void new_user6() {
        name = "Peter";
        hostname = "127.16.0.5";
        port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }
}
