package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.entity.Comment;
import by.training.kolos.entity.User;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.training.kolos.command.ApplicationConstants.*;

public class DeleteCommentCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(SessionRequestContent content) {
        String page;
        Long userId = ((User) content.getSessionAttribute(PARAM_USER)).getId();
        Long commentId = Long.parseLong(content.getRequestParameter(PARAM_COMMENT_ID)[0]);

        try {
            Comment comment = ServiceFactory.getCommentService().find(commentId);
            if (comment == null) {
                logger.log(Level.DEBUG, "Comment not exist");
            } else {
                if (userId.equals(comment.getUserId())) {
                    ServiceFactory.getCommentService().delete(commentId);
                } else {
                    logger.log(Level.DEBUG, "Comment-{} does not belong to the user-{}", commentId, userId);
                }
            }
            page = content.getPreviousPage();
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        } catch (ServiceException e) {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET), PARAM_COMMAND,
                    COMMAND_OPEN_ERROR_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        }
        return page;
    }
}
