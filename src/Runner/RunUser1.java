package src.Runner;

import src.ClientServer.ChatClient;

public class RunUser1 {
    static String name;
    static String hostname;
    static int port;

    public static void main(String[] args) {
        name = "Alice";
        hostname = "127.16.0.1";
        port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }
}
