package ro.vvs.upt.utils;

import ro.vvs.upt.config.Configuration;
import ro.vvs.upt.config.ConfigurationManager;

public class WebServerPath {

    private String rawPath;

    private String requestedPath;
    private String localRequestedPath;

    private Configuration config;

    public WebServerPath(String requestedPath) {
        config = ConfigurationManager.getInstance().getCurrentConfig();

        this.rawPath = requestedPath.replaceAll("%20", " ");
        this.requestedPath = assembleRequestedPath(rawPath);
        this.localRequestedPath = assembleLocalRequestedPath(this.requestedPath);
    }

    public String getRawPath() {
        return rawPath;
    }

    public String getRequestedPath() {
        return requestedPath;
    }

    public String getLocalRequestedPath() {
        return localRequestedPath;
    }

    /**
     * If the parameter is a String that ends with '/', it adds the default page because no resource was specified.
     */
    private String assembleRequestedPath(String rawPath) {
        if (!rawPath.endsWith("/"))
            return rawPath;
        else
            return rawPath + config.getDefaultFile();
    }

    /**
     * Returns the local path of the file requested.
     * @param requestedPath
     * @return
     */
    private String assembleLocalRequestedPath(String requestedPath) {
        if (requestedPath.equals(config.getMaintenanceFile()))
            return concatenatePaths(config.getMaintenanceFolder(), config.getMaintenanceFile());
        return addWebRootPrefix(requestedPath);
    }


    /**
     * Safely concatenates paths so that there's only a slash between them.
     * @param prefix
     * @param path
     * @return
     */
    private static String concatenatePaths(String prefix, String path) {
        return prefix + (path.startsWith("/") ? path : ("/" + path));
    }

    private static String addWebRootPrefix(String path) {
        return concatenatePaths(ConfigurationManager.getInstance().getCurrentConfig().getWebroot(), path);
    }

    public static WebServerPath getErrorWebServerPath() {
        return new WebServerPath(ConfigurationManager.getInstance().getCurrentConfig().getErrorFile());
    }

    public static WebServerPath getMaintenanceWebServerPath() {
        return new WebServerPath(ConfigurationManager.getInstance().getCurrentConfig().getMaintenanceFile());
    }

    public static WebServerPath getDefaultWebServerPath() {
        return new WebServerPath(ConfigurationManager.getInstance().getCurrentConfig().getDefaultFile());
    }
}
