package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.config.MessageManager;
import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.entity.Comment;
import by.training.kolos.entity.User;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;

import static by.training.kolos.command.ApplicationConstants.*;

public class SaveCommentCommand implements AbstractCommand {
    private static Logger logger = LogManager.getLogger();

    @Override
    public String execute(SessionRequestContent content) {
        String page;
        Long photoId = Long.parseLong(content.getRequestParameter(PARAM_PHOTO_ID)[0]);
        Long userId = ((User) content.getSessionAttribute(PARAM_USER)).getId();
        String value = content.getRequestParameter(PARAM_VALUE_COMMENT)[0].replaceAll(JS_TAG, EMPTY_STRING).trim();

        if (value.length() == 0) {
            content.setRequestAttribute(PARAM_EMPTY_COMMENT, MessageManager.getProperty(MESSAGE_EMPTY_COMMENT));
            logger.log(Level.DEBUG, "Comment is empty");
            content.setRequestAttribute(PARAM_PHOTO_ID, photoId);
            page = String.format("%s?%s=%s&%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET), PARAM_COMMAND,
                    COMMAND_OPEN_PHOTO_PAGE,
                    PARAM_PHOTO_ID, photoId);
            content.setDirection(SessionRequestContent.Direction.FORWARD);
            return page;
        }

        Comment comment = Comment.builder()
                .publishDate(Instant.now().toEpochMilli())
                .value(value)
                .photoId(photoId)
                .userId(userId)
                .build();
        try {
            comment = ServiceFactory.getCommentService().save(comment);
            logger.log(Level.DEBUG, "Comment was saved with id-{}", comment.getId());
            page = String.format("%s?%s=%d&%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                    PARAM_PHOTO_ID, photoId, PARAM_COMMAND, COMMAND_OPEN_PHOTO_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        } catch (ServiceException e) {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET), PARAM_COMMAND,
                    COMMAND_OPEN_ERROR_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        }
        return page;
    }
}
