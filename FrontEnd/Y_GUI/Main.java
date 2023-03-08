package FrontEnd.Y_GUI;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String args[]) {
        // Creating the Frame
        JFrame frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700);

        // Creating the friends panel
        DefaultListModel<String> friends_list_model = new DefaultListModel<>();
        JList<String> friends_list = new JList<>(friends_list_model);
        JScrollPane friends_scroll = new JScrollPane(friends_list);
        JPanel friends_panel = new JPanel();
        friends_panel.setLayout(new BorderLayout());
        friends_panel.add(friends_scroll, BorderLayout.CENTER);

        // Creating the chat panel
        JPanel chat_panel = new JPanel();
        chat_panel.setLayout(new BorderLayout());
        JTextArea chat_area = new JTextArea();
        JScrollPane chat_scroll = new JScrollPane(chat_area);
        chat_panel.add(chat_scroll, BorderLayout.CENTER);

        // Creating the typing panel
        JPanel typing_panel = new JPanel();
        typing_panel.setLayout(new BorderLayout());
        JTextField typing_field = new JTextField();
        typing_panel.add(typing_field, BorderLayout.CENTER);

        // Creating the send button
        JButton send_button = new JButton("Send");
        send_button.addActionListener(e -> {
            String message = typing_field.getText();
            chat_area.append(message + "\n");
            typing_field.setText("");
        });
        typing_panel.add(send_button, BorderLayout.EAST);

        // Adding the panels to the frame
        frame.add(friends_panel, BorderLayout.WEST);
        frame.add(chat_panel, BorderLayout.CENTER);
        frame.add(typing_panel, BorderLayout.SOUTH);

        // Adding some friends to the friends panel
        for (int i = 1; i <= 10; i++) {
            friends_list_model.addElement("Friend " + i);
        }

        // Adding a listener for clicks on the friends list
        friends_list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String friend = friends_list.getSelectedValue();
                if (friend != null) {
                    chat_area.setText("Starting conversation with " + friend + "\n");
                }
            }
        });

        frame.setVisible(true);
    }
}
