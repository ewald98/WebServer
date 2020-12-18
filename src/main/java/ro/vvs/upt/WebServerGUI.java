package ro.vvs.upt;

import ro.vvs.upt.config.Configuration;
import ro.vvs.upt.config.ConfigurationManager;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class WebServerGUI extends JFrame {

    private Server server;

    private JPanel infoPanel;
    private JPanel configPanel;
    private JPanel controlPanel;
    private JPanel mainPanel;
    private JLabel status;
    private JLabel address;
    private JLabel port;
    private JTextField portTextField;
    private JTextField rootTextField;
    private JTextField maintenanceTextField;
    private JButton commitChangesButton;
    private JButton selectModeButton;
    private JComboBox comboBox1;

    public WebServerGUI(Server server) {
        super("WebServer");


        this.server = server;
        status.setText(server.getStringServerState());
        try {
            address.setText(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            address.setText("-");
            e.printStackTrace();
        }
        port.setText(server.getPort());


        this.setContentPane(mainPanel);

        configureComboBox();
        selectModeButton.addActionListener(e -> {
            String value = (String)comboBox1.getSelectedItem();
            server.setServerState(value);
        });

        portTextField.setText(server.getPort());
        rootTextField.setText(ConfigurationManager.getInstance().getCurrentConfig().getWebroot());
        maintenanceTextField.setText(ConfigurationManager.getInstance().getCurrentConfig().getMaintenanceFolder());

        commitChangesButton.addActionListener(e -> {
            String port = portTextField.getText();
            String root = rootTextField.getText();
            String maintenanceFolder = maintenanceTextField.getText();
            ConfigurationManager.getInstance().modifyConfigurationFile(port, root, maintenanceFolder);
            if (server.getServerState() == Server.STATUS.STOPPED) {
                ConfigurationManager.getInstance().loadConfigurationFile();
                setPort(port);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("WebServer");
        setSize(600, 480);
    }

    private void configureComboBox() {
        comboBox1.addItem("STOPPED");
        comboBox1.addItem("RUNNING");
        comboBox1.addItem("MAINTENANCE");
    }

    public void setStatus(String newStatus) {
        status.setText(newStatus);
    }

    public void setPort(String newPort) {
        port.setText(newPort);
    }

}
