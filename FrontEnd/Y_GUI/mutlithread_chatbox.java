import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class mutlithread_chatbox extends JFrame {
    JPanel jp;
    JTextField jt;
    JTextArea ta;
    JLabel l;
    boolean typing;
    Timer t;

    public mutlithread_chatbox() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {

        // Set frame properties
        setTitle("mutlithread_chatbox");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create a JPanel and set layout
        jp = new JPanel();
        jp.setLayout(new GridLayout(2, 1));
        l = new JLabel();
        jp.add(l);

        // Create a timer that executes every 1 millisecond
        t = new Timer(1, new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // If the user isn't typing, he is thinking
                if (!typing)
                    l.setText("Thinking..");
            }
        });

        // Set initial delay of 2000 ms
        // That means, actionPerformed() is executed 2500ms
        // after the start() is called
        t.setInitialDelay(2000);

        // Create JTextField, add it.
        jt = new JTextField();
        jp.add(jt);

        // Add panel to the south,
        add(jp, BorderLayout.SOUTH);

        // Add a KeyListener
        jt.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {

                // Key pressed means, the user is typing
                l.setText("You are typing..");

                // When key is pressed, stop the timer
                // so that the user is not thinking, he is typing
                t.stop();

                // He is typing, the key is pressed
                typing = true;

                // If he presses enter, add text to chat textarea
                if (ke.getKeyCode() == KeyEvent.VK_ENTER)
                    showLabel(jt.getText());
            }

            public void keyReleased(KeyEvent ke) {

                // When the user isn't typing..
                typing = false;

                // If the timer is not running, i.e.
                // when the user is not thinking..
                if (!t.isRunning())

                    // He released a key, start the timer,
                    // the timer is started after 2500ms, it sees
                    // whether the user is still in the keyReleased state
                    // which means, he is thinking
                    t.start();
            }
        });

        // Create a textarea
        ta = new JTextArea();

        // Make it non-editable
        ta.setEditable(false);

        // Set some margin, for the text
        ta.setMargin(new Insets(7, 7, 7, 7));

        // Set a scrollpane
        JScrollPane js = new JScrollPane(ta);
        add(js);

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent we) {
                // Get the focus when window is opened
                jt.requestFocus();
            }
        });

        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showLabel(String text) {
        // If text is empty return
        if (text.trim().isEmpty())
            return;

        // Otherwise, append text with a new line
        ta.append(text + "\n");

        // Set textfield and label text to empty string
        jt.setText("");
        l.setText("");
    }

    public static void main(String args[]) {
        // Use the event dispatch thread to build the UI for thread-safety.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new mutlithread_chatbox();
            }
        });
    }
}
