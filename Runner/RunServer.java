package Runner;

import Server.ServerHandler.ChatServer;

public class RunServer {
    public static void main(String[] args) {
        int port = 1234;
        ChatServer server = new ChatServer(port);
        server.execute();
    }
}
