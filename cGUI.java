package COMP1549;
import javax.swing.*;
import java.awt.*;


public class cGUI {

    //constructor
    public cGUI() {
        // JButton object - for buttons on the GUI
        JButton b = new JButton("Send");
        // JTextField object - for text input on the GUI
        JTextField textField = new JTextField(20);

        // JFrame object - main window
        JFrame f = new JFrame("Client GUI");
        // JFrame object - for the set size of the gui window
        f.setPreferredSize(new Dimension(500, 500));
        f.setResizable(false);
        // JPanel object - size of window
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        p.setLayout(new FlowLayout(FlowLayout.RIGHT));
        p.add(b);
        p.add(b,BorderLayout.SOUTH);

        f.add(p, BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
    public static void main(String[] args) {
        new cGUI();
    }
}

