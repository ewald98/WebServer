package ro.vvs.upt;

import ro.vvs.upt.config.ConfigurationManager;

public class GuiMain {

    public static void main(String[] args) {
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/config.txt");

        Server server = new Server();
        server.start();
        WebServerGUI gui = new WebServerGUI(server);
        server.setGui(gui);
        gui.setVisible(true);
    }
}
