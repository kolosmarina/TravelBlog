package by.training.kolos.config;

import by.training.kolos.command.ApplicationConstants;

import java.util.ResourceBundle;

/**
 * Класс для считывания пропертей (пути к jsp)
 *
 * @author Колос Марина
 */
public class ConfigurationManager {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(ApplicationConstants.CONFIG_FILE_NAME);

    private ConfigurationManager() {
    }

    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
