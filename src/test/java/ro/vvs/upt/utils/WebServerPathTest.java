package ro.vvs.upt.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ro.vvs.upt.config.ConfigurationManager;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class WebServerPathTest {

    @BeforeAll
    static void init() {
        ConfigurationManager.getInstance().loadConfigurationFile("src/test/resources/testConfig.txt");
    }

    @Test
    void testGetters() {
        WebServerPath path01 = new WebServerPath("test%2001.html"); // clearly, file
        WebServerPath path02 = new WebServerPath("/upt/vvs/");  // clearly, folder
        WebServerPath path03 = new WebServerPath("/not_working");   // file, but it won't work bc we only allow files with extensions

        assertAll(
                () -> assertEquals("test 01.html", path01.getRawPath()),
                () -> assertEquals("test 01.html", path01.getRequestedPath()),
                () -> assertEquals("src/test/resources/www/test 01.html", path01.getLocalRequestedPath())
        );
        assertAll(
                () -> assertEquals("/upt/vvs/", path02.getRawPath()),
                () -> assertEquals("/upt/vvs/index.html", path02.getRequestedPath()),
                () -> assertEquals("src/test/resources/www/upt/vvs/index.html", path02.getLocalRequestedPath())
        );
        assertAll(
                () -> assertEquals("/not_working", path03.getRawPath()),
                () -> assertEquals("/not_working", path03.getRequestedPath()),
                () -> assertEquals("src/test/resources/www/not_working", path03.getLocalRequestedPath())
        );
    }

    @Test
    void testAddWebRootPrefix() {

        Class c = WebServerPath.class;
        Class[] cArg = new Class[1];
        cArg[0] = String.class;
        try {
            Method method = c.getDeclaredMethod("addWebRootPrefix", cArg);
            method.setAccessible(true);
            assertAll(
                    () -> assertEquals("src/test/resources/www/a.html", method.invoke(null, "a.html")),
                    () -> assertEquals("src/test/resources/www/default.html", method.invoke(null, "default.html"))
            );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    @Test
    void testGetErrorFileLocalPath() {
        assertEquals("src/test/resources/www/404.html", WebServerPath.getErrorWebServerPath().getLocalRequestedPath());
    }

    @Test
    void testGetMaintenanceFileLocalPath() {
        assertEquals("maintenanceFolder/maintenance.html", WebServerPath.getMaintenanceWebServerPath().getLocalRequestedPath());
    }

}