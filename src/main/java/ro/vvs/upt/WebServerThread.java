package ro.vvs.upt;

import ro.vvs.upt.utils.WebServerPath;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.StringTokenizer;

public class WebServerThread extends Thread {

    private final Socket clientSocket;
    private final Server mainServer;

    WebServerThread(Socket clientSoc, Server mainServer) {
        this.clientSocket = clientSoc;
        this.mainServer = mainServer;
        start();
    }

    public void run() {

        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;

        try {
            System.out.println("!!! New Communication Thread Started, on " + clientSocket.getInetAddress());

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());      // for header
            dataOut = new BufferedOutputStream(clientSocket.getOutputStream()); // for requested data

            String input = in.readLine();
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase();

            WebServerPath webServerPath = new WebServerPath(parse.nextToken().toLowerCase());

            System.out.println("here is the request:" + input);

            if (mainServer.getServerState() == Server.STATUS.MAINTENANCE) {
                customResponse(out, dataOut);
//                respond(WebServerPath.getMaintenanceWebServerPath(), out, dataOut);
            } else if (!method.equals("GET") && !method.equals("HEAD")) {  // method not implemented
                respond(WebServerPath.getErrorWebServerPath(), out, dataOut);
            } else {
                if (method.equals("GET")) {
                    respond(webServerPath, out, dataOut);

                    if (webServerPath.getRequestedPath().endsWith(".html") ||
                        webServerPath.getRequestedPath().endsWith(".txt")) {
                        addVisitedPage(clientSocket.getInetAddress(), webServerPath.getRequestedPath());
                    }

                }
            }

        } catch (FileNotFoundException e) { // file not found
            try {
                respond(WebServerPath.getErrorWebServerPath(), out, dataOut);
            } catch (IOException ioException) { }
        } catch (IOException e) {
            System.err.println("Problem with Communication Server");
            System.exit(1);
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) { }
            }
        }
    }

    private void customResponse(PrintWriter out, BufferedOutputStream dataOut) throws IOException {

        String path = "src/main/resources/cache/" + clientSocket.getInetAddress() + ".html";
        File file = new File(path);
        int fileLength = (int) file.length();
        String contentType = getContentType(path);

        byte[] fileData = readFileData(file, fileLength);

        out.println("HTTP/1.1 200 OK");
        out.println("Server: JHTTP Server xDD");
        out.println("Content-type: " + contentType);
        out.println("Content-length: " + fileLength);
        out.println();
        out.flush();

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        System.out.println("File " + path + " of type " + contentType + " returned");

    }

    private void addVisitedPage(InetAddress inetAddress, String requestedPath) {

        Path path = Paths.get("src/main/resources/cache/" + inetAddress + ".html");
        try {
            if (!Files.exists(path)) {
                Path htmlPath = Paths.get(WebServerPath.getMaintenanceWebServerPath().getLocalRequestedPath());
                String htmlHeader = String.join("\n", Files.readAllLines(htmlPath));
                htmlHeader += "<br>";
                Files.write(path, htmlHeader.getBytes(), StandardOpenOption.CREATE);
            }
            Files.write(path, (requestedPath + "<br>\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void respond(WebServerPath webServerPath, PrintWriter out, BufferedOutputStream dataOut) throws IOException {

        File file = new File(webServerPath.getLocalRequestedPath());
        int fileLength = (int) file.length();
        String contentType = getContentType(webServerPath.getRequestedPath());

        byte[] fileData = readFileData(file, fileLength);

        out.println("HTTP/1.1 200 OK");
        out.println("Server: JHTTP Server xDD");
        out.println("Content-type: " + contentType);
        out.println("Content-length: " + fileLength);
        out.println();
        out.flush();

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        System.out.println("File " + webServerPath.getRequestedPath() + " of type " + contentType + " returned");

    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

    private String getContentType(String fileRequested) { // return supported MIME Types
        if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html"))
            return "text/html";
        else
            return "text/plain";
    }

}
