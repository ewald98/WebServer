package ro.vvs.upt;

import ro.vvs.upt.config.Configuration;
import ro.vvs.upt.config.ConfigurationManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultipleRequest {

    public static void main(String[] args) {

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/config.txt");
        PseudoBrowser[] pseudoBrowsers = new PseudoBrowser[100];

        for (int i = 0; i < 100; i++) {
            pseudoBrowsers[i] = new PseudoBrowser("GET / HTTP/1.1\n" +
                    "Host: 127.0.0.1:10008\n" +
                    "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:82.0) Gecko/20100101 Firefox/82.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
                    "Accept-Language: en-US,en;q=0.5\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Cache-Control: max-age=0");
        }

        for (int i = 0; i < 100; i++) {
            pseudoBrowsers[i].start();
        }

        while(true);

    }


    static class PseudoBrowser extends Thread {

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


}
