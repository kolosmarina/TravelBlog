package by.training.kolos.config;

import by.training.kolos.command.ApplicationConstants;

import java.util.ResourceBundle;

/**
 * Класс для считывания пропертей (сообщения клиенту в случае внесения невалидных данных)
 *
 * @author Колос Марина
 */
public class MessageManager {

    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(ApplicationConstants.MESSAGES_FILE_NAME);

    private MessageManager() {
    }

    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
