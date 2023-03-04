
/*
 * https://stackoverflow.com/questions/70229919/how-to-add-a-panel-created-in-another-class-to-a-jframe
 * 
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class panels {

    public static void main(String[] args) {
        new panels();
    }

    public panels() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.add(new MasterPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class MasterPane extends JPanel {

        private MenuPane menuPane;

        public MasterPane() {
            setLayout(new BorderLayout());
            presentMenu();
        }

        protected void presentMenu() {
            removeAll();

            if (menuPane == null) {
                menuPane = new MenuPane(new MenuPane.NavigationListener() {
                    @Override
                    public void presentRedPane(MenuPane source) {
                        RedPane redPane = new RedPane(new ReturnNavigationListener<RedPane>() {
                            @Override
                            public void returnFrom(RedPane source) {
                                presentMenu();
                            }
                        });
                        present(redPane);
                    }

                    @Override
                    public void presentGreenPane(MenuPane source) {
                        GreenPane greenPane = new GreenPane(new ReturnNavigationListener<GreenPane>() {
                            @Override
                            public void returnFrom(GreenPane source) {
                                presentMenu();
                            }
                        });
                        present(greenPane);
                    }

                    @Override
                    public void presentBluePane(MenuPane source) {
                        BluePane bluePane = new BluePane(new ReturnNavigationListener<BluePane>() {
                            @Override
                            public void returnFrom(BluePane source) {
                                presentMenu();
                            }
                        });
                        present(bluePane);
                    }
                });
            }

            add(menuPane);
            revalidate();
            repaint();
        }

        protected void present(JPanel panel) {
            removeAll();
            add(panel);
            revalidate();
            repaint();
        }

    }

    public class MenuPane extends JPanel {

        public static interface NavigationListener {
            public void presentRedPane(MenuPane source);

            public void presentGreenPane(MenuPane source);

            public void presentBluePane(MenuPane source);
        }

        private NavigationListener navigationListener;

        public MenuPane(NavigationListener navigationListener) {
            this.navigationListener = navigationListener;

            setBorder(new EmptyBorder(16, 16, 16, 16));
            setLayout(new GridBagLayout());

            JButton red = new JButton("Red");
            red.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getNavigationListener().presentRedPane(MenuPane.this);
                }
            });
            JButton green = new JButton("Green");
            green.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getNavigationListener().presentGreenPane(MenuPane.this);
                }
            });
            JButton blue = new JButton("Blue");
            blue.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getNavigationListener().presentBluePane(MenuPane.this);
                }
            });

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.BOTH;

            add(red, gbc);
            add(green, gbc);
            add(blue, gbc);
        }

        protected NavigationListener getNavigationListener() {
            return navigationListener;
        }

    }

    public interface ReturnNavigationListener<T> {

        public void returnFrom(T source);
    }

    public class RedPane extends JPanel {

        private ReturnNavigationListener<RedPane> navigationListener;

        public RedPane(ReturnNavigationListener<RedPane> navigationListener) {
            this.navigationListener = navigationListener;
            setBackground(Color.RED);
            setLayout(new BorderLayout());
            add(new JLabel("Roses are red", JLabel.CENTER));

            JButton back = new JButton("Back");
            back.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getReturnNavigationListener().returnFrom(RedPane.this);
                }
            });
            add(back, BorderLayout.SOUTH);
        }

        public ReturnNavigationListener<RedPane> getReturnNavigationListener() {
            return navigationListener;
        }

    }

    public class BluePane extends JPanel {

        private ReturnNavigationListener<BluePane> navigationListener;

        public BluePane(ReturnNavigationListener<BluePane> navigationListener) {
            this.navigationListener = navigationListener;
            setBackground(Color.BLUE);
            setLayout(new BorderLayout());
            add(new JLabel("Violets are blue", JLabel.CENTER));

            JButton back = new JButton("Back");
            back.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getReturnNavigationListener().returnFrom(BluePane.this);
                }
            });
            add(back, BorderLayout.SOUTH);
        }

        public ReturnNavigationListener<BluePane> getReturnNavigationListener() {
            return navigationListener;
        }

    }

    public class GreenPane extends JPanel {

        private ReturnNavigationListener<GreenPane> navigationListener;

        public GreenPane(ReturnNavigationListener<GreenPane> navigationListener) {
            this.navigationListener = navigationListener;
            setBackground(Color.GREEN);
            setLayout(new BorderLayout());
            add(new JLabel("Kermit is green", JLabel.CENTER));

            JButton back = new JButton("Back");
            back.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getReturnNavigationListener().returnFrom(GreenPane.this);
                }
            });
            add(back, BorderLayout.SOUTH);
        }

        public ReturnNavigationListener<GreenPane> getReturnNavigationListener() {
            return navigationListener;
        }

    }
}