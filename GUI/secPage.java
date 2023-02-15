package COMP1549.GUI;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class secPage extends JFrame {
    private JPanel panelMain;
    private JTextField txtName;
    private JButton btnClick;
    public secPage() {
        btnClick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(btnClick,txtName.getAction()+" ,Hello");
            }
        });
    }

    public static void main(String[] args) {
        secPage h=new secPage();
        h.setContentPane(h.panelMain);
        h.setTitle("Hello");
        h.setSize(300,400);
        h.setVisible(true);
        h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
