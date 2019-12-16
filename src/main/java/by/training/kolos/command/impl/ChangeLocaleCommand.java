package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.command.ApplicationConstants;
import by.training.kolos.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChangeLocaleCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(SessionRequestContent content) {
        String page;
        String locale = content.getRequestParameter(ApplicationConstants.PARAM_LOCALE)[0];

        if (locale.equalsIgnoreCase(ApplicationConstants.PARAM_LOCALE_BE_BY)) {
            content.setSessionAttribute(ApplicationConstants.PARAM_LOCALE, ApplicationConstants.PARAM_LOCALE_BE_BY);
        } else if (locale.equalsIgnoreCase(ApplicationConstants.PARAM_LOCALE_EN_US)) {
            content.setSessionAttribute(ApplicationConstants.PARAM_LOCALE, ApplicationConstants.PARAM_LOCALE_EN_US);
        } else {
            content.setSessionAttribute(ApplicationConstants.PARAM_LOCALE, ApplicationConstants.PARAM_LOCALE_RU_RU);
        }
        page = content.getPreviousPage();
        content.setDirection(SessionRequestContent.Direction.REDIRECT);
        logger.log(Level.DEBUG, "Locale set: {}", locale);
        return page;
    }
}
