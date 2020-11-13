package ro.vvs.upt;

import ro.vvs.upt.config.Configuration;
import ro.vvs.upt.config.ConfigurationManager;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class PseudoBrowser extends Thread {

    public static final String ADDRESS = "127.0.0.1";

    private Configuration config;
    private String request;

    private String header = "";

    private String fileContent = "";

    public PseudoBrowser(String request) {
        config = ConfigurationManager.getInstance().getCurrentConfig();
        this.request = request;
    }

    public String getFileContent() {
        return fileContent;
    }

    public String getHeader() {
        return header;
    }

    @Override
    public void run() {

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Socket s = new Socket(ADDRESS, config.getPort());

            PrintWriter out = new PrintWriter(s.getOutputStream());

            out.print(request);
            out.println();
            out.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            int contentLength = 0;

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                header += (inputLine + "\n");
                if (inputLine.startsWith("Content-length:"))
                    contentLength = Integer.parseInt(inputLine.replaceAll("[^0-9.]", ""));

                if (inputLine.trim().equals(""))
                    break;
            }
            while ((inputLine = in.readLine()) != null && fileContent.length() < contentLength) {
                fileContent += (inputLine + "\n");
            }

            out.close();
            in.close();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
