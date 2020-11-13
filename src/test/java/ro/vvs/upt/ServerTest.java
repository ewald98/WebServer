package ro.vvs.upt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.vvs.upt.config.ConfigurationManager;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    private Server server;

    @BeforeEach
    public void setup() {
        ConfigurationManager.getInstance().loadConfigurationFile("src/test/resources/testConfig.txt");

        server = new Server();
        server.setServerState(Server.STATUS.RUNNING);
        server.start();
    }

    @Test
    public void testServerState() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(Server.STATUS.RUNNING, server.getServerState());

        server.setServerState(Server.STATUS.MAINTENANCE);
        assertEquals(Server.STATUS.MAINTENANCE, server.getServerState());

        server.setServerState(Server.STATUS.STOPPED);
        assertEquals(Server.STATUS.STOPPED, server.getServerState());
    }

    @Test
    public void testRequests() {
        PseudoBrowser pseudoBrowser = new PseudoBrowser("GET / HTTP/1.1\n" +
                "Host: 127.0.0.1:10008\n" +
                "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:82.0) Gecko/20100101 Firefox/82.0\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
                "Accept-Language: en-US,en;q=0.5\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: keep-alive\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "Cache-Control: max-age=0");
        pseudoBrowser.start();

        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertAll(
                () -> assertEquals(
                      "HTTP/1.1 200 OK\n" +
                              "Server: JHTTP Server xDD\n" +
                              "Content-type: text/html\n" +
                              "Content-length: 205\n\n",
                       pseudoBrowser.getHeader()
               ),
                () -> assertEquals(
                        "<html>\n" +
                                "<head>\n" +
                                "    <link rel=\"stylesheet\" href=\"mystyle.css\">\n" +
                                "    <title>Page Title</title>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "\n" +
                                "<h1>My First Heading</h1>\n" +
                                "<p>My first paragraph.</p>\n" +
                                "<a href=\"a.html\">go to a</a>\n" +
                                "\n" +
                                "</body>\n" +
                                "</html>\n",
                        pseudoBrowser.getFileContent()
                )
        );

        PseudoBrowser pseudoBrowser2 = new PseudoBrowser("GET /doesntExist.html HTTP/1.1\n" +
                "Host: 127.0.0.1:10008\n" +
                "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:82.0) Gecko/20100101 Firefox/82.0\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
                "Accept-Language: en-US,en;q=0.5\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: keep-alive\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "Cache-Control: max-age=0");
        pseudoBrowser2.start();

        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertAll(
                () -> assertEquals(
                        "HTTP/1.1 200 OK\n" +
                                "Server: JHTTP Server xDD\n" +
                                "Content-type: text/html\n" +
                                "Content-length: 90\n\n",
                        pseudoBrowser2.getHeader()
                ),
                () -> assertEquals(
                        "<html>\n" +
                                "<head>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "<p>Error 404/405: Not found/ Method not supported</p>\n" +
                                "</body>\n",
                        pseudoBrowser2.getFileContent()
                )
        );
    }

}