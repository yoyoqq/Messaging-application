package src.Runner;

import src.ClientServer.ChatClient;

public class RunUser4 {
    static String name;
    static String hostname;
    static int port;

    public static void main(String[] args) {
        name = "Dave";
        hostname = "127.16.0.78";
        port = 1234;

        ChatClient client = new ChatClient(name, hostname, port);
        client.execute();
    }
}
