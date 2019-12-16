package by.training.kolos.config;

import by.training.kolos.command.ApplicationConstants;

import java.util.ResourceBundle;

public class ConfigurationManager {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(ApplicationConstants.CONFIG_FILE_NAME);

       private ConfigurationManager() {
    }

    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
