package COMP1549.GUI;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SignUpPage {

    JFrame frame = new JFrame();
    JLabel label = new JLabel("Sign Up");

    SignUpPage() {
        label.setBounds(100, 50, 100, 30);
        label.setFont(new Font("Arial", Font.PLAIN, 20));


        frame.add(label);
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
