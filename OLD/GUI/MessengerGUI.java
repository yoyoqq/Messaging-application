
package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class MessengerGUI extends JFrame implements ActionListener {
    private JTextField messageField;
    private JTextArea chatArea;
    private JButton sendButton;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public MessengerGUI() {
        // Set up the GUI components
        messageField = new JTextField(30);
        chatArea = new JTextArea(10, 30);
        chatArea.setEditable(false);
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        // Add components to the frame
        JPanel panel = new JPanel();
        panel.add(new JScrollPane(chatArea));
        panel.add(messageField);
        panel.add(sendButton);
        this.add(panel);
        this.pack();

        // Set up the networking
        try {
            socket = new Socket("localhost", 8080); // Change the IP and port as needed
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Start a thread to read messages from the server
            Thread readerThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        String message;
                        while ((message = in.readLine()) != null) {
                            chatArea.append(message + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            readerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            String message = messageField.getText();
            out.println(message);
            messageField.setText("");
        }
    }

    public static void main(String[] args) {
        MessengerGUI messengerGUI = new MessengerGUI();
        messengerGUI.setVisible(true);
        messengerGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
