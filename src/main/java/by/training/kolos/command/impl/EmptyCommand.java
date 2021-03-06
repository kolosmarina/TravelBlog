package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.command.ApplicationConstants;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс для выполнения пустого запроса от пользователя
 *
 * @author Колос Марина
 */
public class EmptyCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();

    /**
     * @see AbstractCommand#execute(SessionRequestContent)
     */
    @Override
    public String execute(SessionRequestContent content) {
        String page = String.format("%s?%s=%s", ConfigurationManager.getProperty(ApplicationConstants.MAIN_SERVLET),
                ApplicationConstants.PARAM_COMMAND, ApplicationConstants.COMMAND_OPEN_MAIN_PAGE);
        content.setDirection(SessionRequestContent.Direction.REDIRECT);
        logger.log(Level.DEBUG, "EmptyCommand is completed");
        return page;
    }
}
