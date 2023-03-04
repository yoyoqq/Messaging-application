package Server.Database;

import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
import java.util.Scanner;

class Message {
    String time;
    User user;
    String text;

    public Message(User user, String text) {
        this.user = user;
        this.text = text;
        this.time = getTime();
    }

    String getTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
