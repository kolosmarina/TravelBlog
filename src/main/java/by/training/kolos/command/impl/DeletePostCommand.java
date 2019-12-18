package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.entity.Post;
import by.training.kolos.entity.User;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.training.kolos.command.ApplicationConstants.*;

/**
 * Класс для выполнения запроса по удалению авторизированным пользователем собственного поста
 *
 * @author Колос Марина
 */
public class DeletePostCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();

    /**
     * @see AbstractCommand#execute(SessionRequestContent)
     */
    @Override
    public String execute(SessionRequestContent content) {
        String page;
        Long postId = Long.parseLong(content.getRequestParameter(PARAM_POST_ID)[0]);
        Long userId = ((User) content.getSessionAttribute(PARAM_USER)).getId();
        try {

            Post post = ServiceFactory.getPostService().find(postId);
            if (post == null) {
                logger.log(Level.DEBUG, "DeletePostCommand: post not exist");
            } else {
                checkRightsAndDelete(postId, userId, post);
            }
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET), PARAM_COMMAND,
                    COMMAND_OPEN_SUCCESS_DELETE_POST_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        } catch (ServiceException e) {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET), PARAM_COMMAND,
                    COMMAND_OPEN_ERROR_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        }
        return page;
    }

    private void checkRightsAndDelete(Long postId, Long userId, Post post) throws ServiceException {
        if (userId.equals(post.getUserId())) {
            ServiceFactory.getPostService().delete(postId);
        } else {
            logger.log(Level.DEBUG, "Post-{} does not belong to the user-{}", postId, userId);
        }
    }
}
