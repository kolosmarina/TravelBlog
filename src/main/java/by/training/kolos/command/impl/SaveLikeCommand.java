package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.entity.Like;
import by.training.kolos.entity.User;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.training.kolos.command.ApplicationConstants.*;

/**
 * Класс для выполнения запроса по сохранению лайка авторизированным пользователем
 *
 * @author Колос Марина
 */
public class SaveLikeCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();

    /**
     * @see AbstractCommand#execute(SessionRequestContent)
     */
    @Override
    public String execute(SessionRequestContent content) {
        String page;
        Long photoId = Long.parseLong(content.getRequestParameter(PARAM_PHOTO_ID)[0]);
        User user = (User) content.getSessionAttribute(PARAM_USER);
        try {

            Like like = ServiceFactory.getLikeService().find(photoId, user.getId());
            if (like != null) {
                logger.log(Level.DEBUG, "Like already exists for photoId-{} and userId-{}", photoId, user.getId());
            } else {
                Like newLike = Like.builder().photoId(photoId).userId(user.getId()).build();
                ServiceFactory.getLikeService().save(newLike);
            }
            page = content.getPreviousPage();
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        } catch (ServiceException e) {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                    PARAM_COMMAND, COMMAND_OPEN_ERROR_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        }
        return page;
    }
}
