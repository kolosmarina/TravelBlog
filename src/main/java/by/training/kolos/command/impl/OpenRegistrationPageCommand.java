package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.command.ApplicationConstants;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс для выполнения запроса по получению доступа к странице регистрации нового пользователя
 *
 * @author Колос Марина
 */
public class OpenRegistrationPageCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();

    /**
     * @see AbstractCommand#execute(SessionRequestContent)
     */
    @Override
    public String execute(SessionRequestContent content) {
        String page;

        //to display a message when user has successfully registered (page reuse)
        String paramSuccessRegistration = content.getRequestParameter(ApplicationConstants.PARAM_SUCCESS_REGISTRATION) != null
                ? content.getRequestParameter(ApplicationConstants.PARAM_SUCCESS_REGISTRATION)[0]
                : null;

        if (content.getSessionAttribute(ApplicationConstants.PARAM_USER) != null) {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(ApplicationConstants.MAIN_SERVLET),
                    ApplicationConstants.PARAM_COMMAND, ApplicationConstants.COMMAND_OPEN_MAIN_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
            logger.log(Level.DEBUG, "Authenticated user is trying to access Registration page. Redirect to Main page");
        } else {
            //to display a message when user has successfully registered (page reuse)
            content.setRequestAttribute(ApplicationConstants.PARAM_SUCCESS_REGISTRATION, paramSuccessRegistration);
            content.setDirection(SessionRequestContent.Direction.FORWARD);
            page = ConfigurationManager.getProperty(ApplicationConstants.PAGE_REGISTRATION);
        }
        return page;
    }
}
