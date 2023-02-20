package COMP1549.GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class MainPage extends JFrame {

    JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signUpButton;
    private JButton signInButton;
    private JLabel displayField;
    private ImageIcon backgroundImage;


    public MainPage() {
        // set the frame properties
        frame = new JFrame("JJJYN Messenger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);


        // create the UI components
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        signInButton = new JButton("Sign Up");
        signUpButton = new JButton("Sign In");
        backgroundImage = new ImageIcon(this.getClass().getResource("/views.jpeg"));
        displayField = new JLabel(backgroundImage);
        frame.add(displayField);



        // add action listener to sign up button
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // handle sign up functionality here
                // get the username and password fields' values
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                // perform any necessary verification or validation here
                // add user to database or perform other necessary functionality
            }
        });

        // add action listener to sign up button
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // handle sign up functionality here
                // get the username and password fields' values
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                // perform any necessary verification or validation here
                // add user to database or perform other necessary functionality
            }
        });

        // create the layout and add the components to the frame
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(usernameLabel, gbc);
        gbc.gridy++;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);
        gbc.gridy++;
        panel.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(signInButton, gbc);
        gbc.gridx++;
        gbc.gridwidth = 2;
        panel.add(signUpButton, gbc);

        // show the frame
        frame.pack();
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new MainPage();
    }
}

