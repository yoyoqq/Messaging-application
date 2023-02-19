package COMP1549.GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signUpButton;
    private JButton signInButton;

    public SignUpPage() {
        // set the frame properties
        setTitle("JJJYN Messenger");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // create the UI components
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        signUpButton = new JButton("Sign Up");
        signInButton = new JButton("Sign In");

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
        panel.add(signUpButton, gbc);

        // show the frame
        setContentPane(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SignUpPage();
    }
}

