package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.entity.Photo;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static by.training.kolos.command.ApplicationConstants.*;

/**
 * Класс для выполнения запроса по доступу к странице поста
 *
 * @author Колос Марина
 */
public class OpenPostPageCommand implements AbstractCommand {
    private static Logger logger = LogManager.getLogger();
    /**
     * Количество фото одновременно отображаемых на странице
     */
    private static final int photosNumberLimit = 3;

    /**
     * @see AbstractCommand#execute(SessionRequestContent)
     */
    @Override
    public String execute(SessionRequestContent content) {
        String page;
        Long postId = Long.parseLong(content.getRequestParameter(PARAM_POST_ID)[0]);
        int pageNumber = content.getRequestParameter(PARAM_PAGE_NUMBER) != null
                ? Integer.parseInt(content.getRequestParameter(PARAM_PAGE_NUMBER)[0])
                : 1;
        //to reuse the command and page about choosing the main photo
        boolean isNewPost = content.getRequestParameter(PARAM_IS_NEW_POST) != null
                && Boolean.parseBoolean(content.getRequestParameter(PARAM_IS_NEW_POST)[0]);
        logger.log(Level.DEBUG, "Is new Post - {}", isNewPost);

        try {
            List<Photo> photos = ServiceFactory.getPhotoService().findPhotosWithLikesNumber(photosNumberLimit,
                    (pageNumber - 1) * photosNumberLimit, postId);
            long photosNumber = ServiceFactory.getPhotoService().countPhotosInPost(postId);
            long postUserId = ServiceFactory.getPostService().findUserId(postId);
            content.setRequestAttribute(PARAM_PHOTOS, photos);
            int lastPageNumber = photosNumber % photosNumberLimit == 0
                    ? (int) (photosNumber / photosNumberLimit)
                    : (int) (photosNumber / photosNumberLimit + 1);
            content.setRequestAttribute(PARAM_PREV_PAGE_NUMBER, pageNumber - 1);
            content.setRequestAttribute(PARAM_NEXT_PAGE_NUMBER, pageNumber + 1);
            content.setRequestAttribute(PARAM_LAST_PAGE_NUMBER, lastPageNumber);
            content.setRequestAttribute(PARAM_POST_ID, postId);
            content.setRequestAttribute(PARAM_POST_USER_ID, postUserId);
            content.setRequestAttribute(PARAM_IS_NEW_POST, isNewPost);
            page = ConfigurationManager.getProperty(PAGE_POST);
            content.setDirection(SessionRequestContent.Direction.FORWARD);
        } catch (ServiceException e) {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                    PARAM_COMMAND, COMMAND_OPEN_ERROR_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        }
        return page;
    }
}
