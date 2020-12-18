package ro.vvs.upt;

import ro.vvs.upt.config.Configuration;
import ro.vvs.upt.config.ConfigurationManager;

import java.io.*;
import java.net.ServerSocket;

public class Server extends Thread {

    private WebServerGUI gui;

    public enum STATUS {STOPPED, RUNNING, MAINTENANCE}

    private STATUS serverState;
    private ServerSocket serverSocket = null;

    public Server() {
        // server is stopped initially
        serverState = STATUS.STOPPED;
    }

    public STATUS getServerState() {
        return serverState;
    }

    public String getStringServerState() {
        return serverState.toString();
    }

    public String getPort() {
        return String.valueOf(ConfigurationManager.getInstance().getCurrentConfig().getPort());
    }

    public void setServerState(STATUS serverState) {
        this.serverState = serverState;

        // to stop listening we need to close the serverSocket
        if (serverState == STATUS.STOPPED) {
            try {
                if (serverSocket != null)
                    serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setServerState(String state) {
        if (state.equals("STOPPED"))
            setServerState(STATUS.STOPPED);
        if (state.equals("RUNNING"))
            setServerState(STATUS.RUNNING);
        if (state.equals("MAINTENANCE"))
            setServerState(STATUS.MAINTENANCE);
    }

    public void reloadConfig() {
        // TODO: in case server is stopped
    }

    @Override
    public void run() {

        while (true) {

            if (gui != null) gui.setStatus(serverState.toString());
            if (serverState != STATUS.STOPPED) {
                try {
                    serverSocket = new ServerSocket(ConfigurationManager.getInstance().getCurrentConfig().getPort());
                    System.out.println("Connection Socket Created");
                    try {
                        while (true) {
                            if (gui != null) gui.setStatus(serverState.toString());
                            System.out.println("Waiting for Connection");
                            new WebServerThread(serverSocket.accept(), this);
                        }
                    } catch (IOException e) {
                        System.err.println("Accept failed.");
                    }
                } catch (IOException e) {
                    System.err.println("Could not listen on port: " + ConfigurationManager.getInstance().getCurrentConfig().getPort());
                    System.exit(1);
                } finally {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        System.err.println("Could not close port: " + ConfigurationManager.getInstance().getCurrentConfig().getPort());
                        System.exit(1);
                    }
                }
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void setGui(WebServerGUI gui) {
        this.gui = gui;
    }

}
