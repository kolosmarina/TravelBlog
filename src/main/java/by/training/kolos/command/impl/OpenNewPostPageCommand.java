package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.entity.*;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static by.training.kolos.command.ApplicationConstants.*;

public class OpenNewPostPageCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();

    private static final int tagsNumberOffset = 8;
    private static final int photosNumberAvailableForUpload = 3;

    @Override
    public String execute(SessionRequestContent content) {
        String page;
        String worldPart = content.getRequestParameter(PARAM_WORLD_PART)[0];
        Long postId = content.getRequestParameter(PARAM_POST_ID) != null
                ? Long.valueOf(content.getRequestParameter(PARAM_POST_ID)[0])
                : null;

        //to display a message to the user when creating a post without the necessary data
        String paramEmptyPostName = content.getRequestParameter(PARAM_EMPTY_POST_NAME) != null
                ? content.getRequestParameter(PARAM_EMPTY_POST_NAME)[0]
                : null;
        String paramEmptyFileForDownload = content.getRequestParameter(PARAM_EMPTY_FILES) != null
                ? content.getRequestParameter(PARAM_EMPTY_FILES)[0]
                : null;

        try {
            List<Tag> tags = ServiceFactory.getTagService().findPopularTagsByWorldPart(tagsNumberOffset, worldPart);
            //to display a message to the user when creating a post without the necessary data
            content.setRequestAttribute(PARAM_EMPTY_POST_NAME, paramEmptyPostName);
            content.setRequestAttribute(PARAM_EMPTY_FILES, paramEmptyFileForDownload);

            content.setRequestAttribute(PARAM_WORLD_PART, worldPart);
            content.setRequestAttribute(PARAM_TAGS, tags);
            content.setRequestAttribute(PARAM_PHOTOS_NUMBER_AVAILABLE_FOR_UPLOAD, photosNumberAvailableForUpload);
            content.setRequestAttribute(PARAM_POST_ID, postId);
            page = ConfigurationManager.getProperty(PAGE_NEW_POST);
            content.setDirection(SessionRequestContent.Direction.FORWARD);
        } catch (ServiceException e) {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                    PARAM_COMMAND, COMMAND_OPEN_ERROR_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        }
        return page;
    }
}
