package ro.vvs.upt.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

public class ConfigurationManager {

    private static ConfigurationManager configurationManager;
    private static Configuration currentConfig;
    private static String filePath;

    private  ConfigurationManager() { }

    public static ConfigurationManager getInstance() {

        if (configurationManager == null)
            configurationManager = new ConfigurationManager();

        return configurationManager;
    }

    public void loadConfigurationFile(String filePath) throws ConfigurationFileException {
        ConfigurationManager.filePath = filePath;

        ArrayList<String> lines = new ArrayList<String>();
        Scanner input = null;

        try {
            input = new Scanner(new File(filePath)).useDelimiter("\n");
        } catch (FileNotFoundException e) {
            throw new ConfigurationFileException("file not found");
        }

        while(input.hasNextLine())
        {
            Scanner colReader = new Scanner(input.nextLine());
            while(colReader.hasNext())
            {
                lines.add(colReader.nextLine());
            }
        }

        if (lines.size() != 6) throw new ConfigurationFileException("invalid configuration file");

        currentConfig = new Configuration(Integer.parseInt(lines.get(0)), lines.get(1), lines.get(2), lines.get(3), lines.get(4), lines.get(5));

    }

    public void loadConfigurationFile() {
        if (filePath != null) loadConfigurationFile(filePath);
    }

    public void modifyConfigurationFile(String port, String root, String maintenanceFolder) {
        Configuration newConfig = new Configuration(
                Integer.parseInt(port),
                root,
                currentConfig.getMaintenanceFile(),
                currentConfig.getDefaultFile(),
                currentConfig.getErrorFile(),
                maintenanceFolder
        );

        try {
            File file = new File(filePath);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(newConfig.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getCurrentConfig() {
        if (currentConfig == null)
            throw new ConfigurationFileException("No config set");

        return currentConfig;
    }

}
