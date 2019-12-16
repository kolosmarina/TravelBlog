package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.training.kolos.command.ApplicationConstants.*;

public class SetMainPhotoCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(SessionRequestContent content) {
        String page;
        Long photoId = Long.parseLong(content.getRequestParameter(PARAM_MAIN_PHOTO)[0]);
        Long postId = Long.parseLong(content.getRequestParameter(PARAM_POST_ID)[0]);
        try {
            ServiceFactory.getPhotoService().changeMainPhoto(photoId);
            logger.log(Level.DEBUG, "For new Post set main photo");
            page = String.format("%s?%s=%d&%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET), PARAM_POST_ID,
                    postId, PARAM_COMMAND,
                    COMMAND_OPEN_POST_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        } catch (ServiceException e) {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET), PARAM_COMMAND,
                    COMMAND_OPEN_ERROR_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        }
        return page;
    }
}
