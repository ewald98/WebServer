package ro.vvs.upt.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ConfigurationManager {

    private static ConfigurationManager configurationManager;
    private static Configuration currentConfig;

    private  ConfigurationManager() { }

    public static ConfigurationManager getInstance() {

        if (configurationManager == null)
            configurationManager = new ConfigurationManager();

        return configurationManager;
    }

    public void loadConfigurationFile(String filePath) throws ConfigurationFileException {

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

    public Configuration getCurrentConfig() {
        if (currentConfig == null)
            throw new ConfigurationFileException("No config set");

        return currentConfig;
    }

}
