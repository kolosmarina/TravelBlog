package by.training.kolos.command;

import by.training.kolos.config.MessageManager;
import by.training.kolos.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static by.training.kolos.command.ApplicationConstants.*;

/**
 * Класс используется для валидации полученных данных от клиента
 *
 * @author Колос Марина
 */
public final class InputDataValidation {
    private static final Logger logger = LogManager.getLogger();
    /**
     * Паттерн для email: состоит из латинских букв, цифр, знака подчеркивания, точки, тире, @ («собака»)
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("(^[\\w-]+\\.)*[\\w-]+@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,6}");
    /**
     * Паттерн для пароля: состоит не менее чем из 5 и не более чем из 25 символов,
     * включает цифры, латинские буквы и знак подчеркивания,
     * содержит хотя бы одно число, одну латинскую букву в нижнем регистре и одну латинскую букву в верхнем регистре
     */
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[\\w]{5,25}$");
    /**
     * Паттерн для nickname: начинается с латинской буквы,
     * состоит не менее чем из 5 и не более чем из 20 символов,
     * включает цифры, латинские буквы и знак подчеркивания
     */
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[A-Za-z][\\w]{4,20}$");

    private InputDataValidation() {
    }

    /**
     * Метод для валидации полученных данных от клиента до соединения с базой данных
     * Добавление атрибутов (сообщений об ошибках) в объект {@link SessionRequestContent} при невалидном значении
     * данных для отображения клиенту в response
     *
     * @param content содержит всю информацию из request от клиента
     * @return true, если все введенные данные прошли валидацию успешно
     */
    public static boolean inputValidationForAuthentication(SessionRequestContent content) {
        boolean validationResult = true;
        if (!InputDataValidation.isValidEmail(content.getRequestParameter(PARAM_EMAIL)[0])) {
            content.setRequestAttribute(PARAM_LOGIN_OR_PASSWORD_ERROR, MessageManager.getProperty(MESSAGE_LOGIN_OR_PASSWORD_INCORRECT));
            validationResult = false;
            logger.log(Level.DEBUG, "Input login is not valid");
        }
        if (!InputDataValidation.isValidPassword(content.getRequestParameter(PARAM_PASSWORD)[0])) {
            content.setRequestAttribute(PARAM_LOGIN_OR_PASSWORD_ERROR, MessageManager.getProperty(MESSAGE_LOGIN_OR_PASSWORD_INCORRECT));
            validationResult = false;
            logger.log(Level.DEBUG, "Input password is not valid");
        }
        return validationResult;
    }

    /**
     * @see #inputValidationForAuthentication(SessionRequestContent)
     */
    public static boolean inputValidationForRegistration(SessionRequestContent content) {
        boolean validationResult = true;
        if (!InputDataValidation.isValidEmail(content.getRequestParameter(PARAM_EMAIL)[0])) {
            content.setRequestAttribute(PARAM_INVALID_EMAIL,
                    MessageManager.getProperty(MESSAGE_REGISTRATION_INVALID_EMAIL_ERROR));
            validationResult = false;
            logger.log(Level.DEBUG, "Input email is not valid");
        }
        if (!InputDataValidation.isValidPassword(content.getRequestParameter(PARAM_PASSWORD)[0])) {
            content.setRequestAttribute(PARAM_INVALID_PASSWORD,
                    MessageManager.getProperty(MESSAGE_REGISTRATION_INVALID_PASSWORD_ERROR));
            validationResult = false;
            logger.log(Level.DEBUG, "Input password is not valid");
        }
        if (!InputDataValidation.isValidNickname(content.getRequestParameter(PARAM_NICKNAME)[0])) {
            content.setRequestAttribute(PARAM_INVALID_NICKNAME,
                    MessageManager.getProperty(MESSAGE_REGISTRATION_INVALID_NICKNAME_ERROR));
            validationResult = false;
            logger.log(Level.DEBUG, "Input nickname is not valid");
        }
        return validationResult;
    }

    private static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email.trim());
        return matcher.matches();
    }

    private static boolean isValidPassword(String password) {
        Matcher matcher = PASSWORD_PATTERN.matcher(password.trim());
        return matcher.matches();
    }

    private static boolean isValidNickname(String nickname) {
        Matcher matcher = NICKNAME_PATTERN.matcher(nickname.trim());
        return matcher.matches();
    }
}
