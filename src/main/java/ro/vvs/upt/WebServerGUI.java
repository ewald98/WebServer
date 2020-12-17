package ro.vvs.upt;

import javax.swing.*;

public class WebServerGUI extends JFrame {

    private JPanel infoPanel;
    private JPanel configPanel;
    private JPanel controlPanel;
    private JPanel mainPanel;
    private JLabel status;
    private JLabel address;
    private JLabel port;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton commitChangesButton;

    public WebServerGUI() {
        super("WebServer");
        this.setContentPane(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("WebServer");
        pack();
    }

}
