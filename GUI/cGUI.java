import javax.swing.*;
import java.awt.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class cGUI { /*implements ActionListener {*/

    //constructor
    public cGUI() {
        // JButton object - for buttons on the GUI
        JButton siButton = new JButton("Sign In");
        JButton suButton = new JButton("Sign Up");

        // JTextField object - for text input on the GUI
        JTextField userTf = new JTextField(20);
        userTf.setBounds(100, 20, 165, 25);


        // JFrame object - main window
        JFrame frame = new JFrame("Client GUI");
        JPanel panel = new JPanel();
        // JFrame object - for the set size of the gui window
        frame.setPreferredSize(new Dimension(500, 500));
        // this is to stop the user from resizing the window
        frame.setResizable(false);
        // JFRAME object - top of gui outline colour set to orange
        frame.setBackground(Color.ORANGE);
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        frame.add(panel);

        panel.setBackground(Color.ORANGE);
        panel.setLayout(null);
        // JPanel object - size of window
        //JPanel p = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        // brings the panel to the front of the frame
        //panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.add(siButton);
        //brings the button "send" to the bottom of the gui
        //panel.add(button,BorderLayout.SOUTH);

        // Label "username"
        JLabel unLabel = new JLabel("Username");
        unLabel.setBounds(10, 20, 80, 25);
        userTf.setBounds(100, 20, 165, 25);
        panel.add(unLabel);
        panel.add(userTf);

        // Label "password"
        JLabel pwLabel = new JLabel("Password");
        pwLabel.setBounds(10, 50, 80, 25);
        panel.add(pwLabel);
        // Text field for password
        JPasswordField pwTf = new JPasswordField();
        pwTf.setBounds(100, 50, 165, 25);
        panel.add(pwTf);
        // sign in button
        siButton.setBounds(10, 80, 80, 25);
        //siButton.addActionListener(new cGUI());
        panel.add(siButton);
        // sign up button
        suButton.setBounds(100, 80, 80, 25);
        panel.add(suButton);


        frame.setVisible(true);

    }
    public static void main(String[] args) {
        new cGUI();
    }

   /* @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Signed In Succesfully");

    }*/
}