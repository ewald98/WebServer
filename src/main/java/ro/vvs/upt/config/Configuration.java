package ro.vvs.upt.config;

import java.util.Objects;

public class Configuration {

    private int port;
    private String webroot;
    private String maintenanceFile;
    private String errorFile;
    private String defaultFile;

    private String maintenanceFolder;

    public Configuration(int port, String webroot, String maintenanceFile, String defaultFile, String errorFile, String maintenanceFolder) {
        this.port = port;
        this.webroot = webroot;
        this.maintenanceFile = maintenanceFile;
        this.defaultFile = defaultFile;
        this.errorFile = errorFile;
        this.maintenanceFolder = maintenanceFolder;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getWebroot() {
        return webroot;
    }

    public void setWebroot(String webroot) {
        this.webroot = webroot;
    }

    public String getMaintenanceFile() {
        return maintenanceFile;
    }

    public void setMaintenanceFile(String maintenanceFile) {
        this.maintenanceFile = maintenanceFile;
    }

    public String getErrorFile() {
        return errorFile;
    }

    public void setErrorFile(String errorFile) {
        this.errorFile = errorFile;
    }

    public String getDefaultFile() {
        return defaultFile;
    }

    public void setDefaultFile(String defaultFile) {
        this.defaultFile = defaultFile;
    }

    public String getMaintenanceFolder() {
        return maintenanceFolder;
    }

    public void setMaintenanceFolder(String maintenanceFolder) {
        this.maintenanceFolder = maintenanceFolder;
    }
}
