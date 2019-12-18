package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.command.InputDataValidation;
import by.training.kolos.command.PasswordSecurity;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.config.MessageManager;
import by.training.kolos.entity.User;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import by.training.kolos.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.training.kolos.command.ApplicationConstants.*;

/**
 * Класс для выполнения запроса по аутентификации пользователя в системе
 *
 * @author Колос Марина
 */
public class AuthenticationCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();

    /**
     * @see AbstractCommand#execute(SessionRequestContent)
     */
    @Override
    public String execute(SessionRequestContent content) {
        String page;

        if (!InputDataValidation.inputValidationForAuthentication(content)) {
            page = ConfigurationManager.getProperty(PAGE_LOGIN);
            content.setDirection(SessionRequestContent.Direction.FORWARD);
            return page;
        }

        String email = content.getRequestParameter(PARAM_EMAIL)[0].trim().toLowerCase();
        String hashedPassword = PasswordSecurity.getHashedPassword(content.getRequestParameter(PARAM_PASSWORD)[0].trim());
        try {
            User user = ServiceFactory.getUserService().find(email, hashedPassword);
            if (user != null) {
                page = getPageByUserStatus(content, user);
            } else {
                page = ConfigurationManager.getProperty(PAGE_LOGIN);
                content.setDirection(SessionRequestContent.Direction.FORWARD);
                content.setRequestAttribute(PARAM_LOGIN_OR_PASSWORD_ERROR,
                        MessageManager.getProperty(MESSAGE_LOGIN_OR_PASSWORD_INCORRECT));
                logger.log(Level.DEBUG, "User entered invalid data");
            }
        } catch (ServiceException e) {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET), PARAM_COMMAND,
                    COMMAND_OPEN_ERROR_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        }
        return page;
    }

    private String getPageByUserStatus(SessionRequestContent content, User user) {
        String page;
        if (user.getIsActive()) {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET), PARAM_COMMAND,
                    COMMAND_OPEN_MAIN_PAGE);
            content.setSessionAttribute(PARAM_USER, user);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        } else {
            page = ConfigurationManager.getProperty(PAGE_LOGIN);
            content.setDirection(SessionRequestContent.Direction.FORWARD);
            content.setRequestAttribute(PARAM_USER_BLOCKED, MessageManager.getProperty(MESSAGE_USER_BLOCKED));
            logger.log(Level.DEBUG, "User-{} is blocked", user.getNickname());
        }
        return page;
    }
}
