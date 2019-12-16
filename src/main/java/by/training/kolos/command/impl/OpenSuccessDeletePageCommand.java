package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.command.ApplicationConstants;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OpenSuccessDeletePageCommand implements AbstractCommand {
    private static Logger logger = LogManager.getLogger();

    @Override
    public String execute(SessionRequestContent content) {
        String page;
        page = ConfigurationManager.getProperty(ApplicationConstants.PAGE_SUCCESS_DELETE_POST);
        content.setDirection(SessionRequestContent.Direction.FORWARD);
        logger.log(Level.DEBUG, "OpenSuccessDeletePageCommand is completed");
        return page;
    }
}
