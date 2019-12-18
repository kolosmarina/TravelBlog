package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.command.ApplicationConstants;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс для выполнения выхода из системы авторизированным пользователем
 *
 * @author Колос Марина
 */
public class LogoutCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();

    /**
     * @see AbstractCommand#execute(SessionRequestContent)
     */
    @Override
    public String execute(SessionRequestContent content) {
        String page = ConfigurationManager.getProperty(ApplicationConstants.PAGE_INDEX);
        content.setDirection(SessionRequestContent.Direction.REDIRECT);
        content.invalidateSession();
        logger.log(Level.DEBUG, "LogoutCommand is completed");
        return page;
    }
}
