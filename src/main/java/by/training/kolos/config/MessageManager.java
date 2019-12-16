package by.training.kolos.config;

import by.training.kolos.command.ApplicationConstants;

import java.util.ResourceBundle;

public class MessageManager {

    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(ApplicationConstants.MESSAGES_FILE_NAME);

      private MessageManager() {
    }

    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
