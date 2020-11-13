package ro.vvs.upt;

import ro.vvs.upt.config.Configuration;
import ro.vvs.upt.config.ConfigurationManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/config.txt");

        Server server = new Server();
        server.start();

        while (true) {
            System.out.println("Change Server State:");
            System.out.println("0. STOPPED");
            System.out.println("1. RUNNING");
            System.out.println("2. MAINTENANCE");
            Scanner in = new Scanner(System.in);
            String state = in.next();

            switch (state) {
                case "0":
                    server.setServerState(Server.STATUS.STOPPED);
                    break;
                case "1":
                    server.setServerState(Server.STATUS.RUNNING);
                    break;
                case "2":
                    server.setServerState(Server.STATUS.MAINTENANCE);
                    break;
                default:
                    System.out.println("Unrecognized state. Bye!");
            }
        }
    }

}
