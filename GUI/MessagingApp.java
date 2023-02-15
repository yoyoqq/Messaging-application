package COMP1549.GUI;
import javax.swing.*;
import java.awt.*;

public class MessagingApp extends JFrame {

    private JTextField inputField;
    private JButton sendButton;

    public MessagingApp() {
        // set the frame properties
        setTitle("Messaging App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);

        // create the UI components
        inputField = new JTextField();
        sendButton = new JButton("Send");

        // add the components to the frame
        add(inputField, BorderLayout.SOUTH);
        add(sendButton, BorderLayout.EAST);

        // show the frame
        setVisible(true);
    }

    public static void main(String[] args) {
        new MessagingApp();
    }
}
