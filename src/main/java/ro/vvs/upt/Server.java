package ro.vvs.upt;

import ro.vvs.upt.config.Configuration;
import ro.vvs.upt.config.ConfigurationManager;

import java.io.*;
import java.net.ServerSocket;

public class Server extends Thread {

    public enum STATUS {STOPPED, RUNNING, MAINTENANCE}

    private STATUS serverState;
    private ServerSocket serverSocket = null;

    private final Configuration config;


    public Server() {
        config = ConfigurationManager.getInstance().getCurrentConfig();

        // server is stopped initially
        serverState = STATUS.STOPPED;
    }

    public STATUS getServerState() {
        return serverState;
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

    @Override
    public void run() {

        while (true) {

            if (serverState != STATUS.STOPPED) {
                try {
                    serverSocket = new ServerSocket(config.getPort());
                    System.out.println("Connection Socket Created");
                    try {
                        while (true) {
                            System.out.println("Waiting for Connection");
                            new WebServerThread(serverSocket.accept(), this);
                        }
                    } catch (IOException e) {
                        System.err.println("Accept failed.");
                    }
                } catch (IOException e) {
                    System.err.println("Could not listen on port: " + config.getPort());
                    System.exit(1);
                } finally {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        System.err.println("Could not close port: " + config.getPort());
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

}
