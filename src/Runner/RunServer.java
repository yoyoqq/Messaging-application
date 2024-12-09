package src.Runner;

import src.Server.ServerHandler.ChatServer;

// This class is responsible of running a server on a specific port
public class RunServer {
    public static void main(String[] args) {
        int port = 1234;
        ChatServer server = new ChatServer(port);
        server.execute();
    }
}
