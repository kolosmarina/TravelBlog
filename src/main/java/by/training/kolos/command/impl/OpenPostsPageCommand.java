package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.entity.Post;
import by.training.kolos.entity.Tag;
import by.training.kolos.entity.User;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static by.training.kolos.command.ApplicationConstants.*;

/**
 * Класс для выполнения запроса по доступу к странице постов
 *
 * @author Колос Марина
 */
public class OpenPostsPageCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();
    /**
     * количество постов (главных фото) одновременно отображаемых на странице
     */
    private static final int postsNumberLimit = 6;
    /**
     * количество пользователей (nickname) одновременно отображаемых на странице
     */
    private static final int usersNumberLimit = 5;
    /**
     * количество тегов одновременно отображаемых на странице
     */
    private static final int tagsNumberLimit = 5;

    /**
     * @see AbstractCommand#execute(SessionRequestContent)
     */
    @Override
    public String execute(SessionRequestContent content) {
        String page;
        List<Post> posts;
        String worldPart = content.getRequestParameter(PARAM_WORLD_PART)[0];
        String sortBy = content.getRequestParameter(PARAM_SORT_BY) != null
                ? content.getRequestParameter(PARAM_SORT_BY)[0]
                : PARAM_DATE;
        int pageNumber = content.getRequestParameter(PARAM_PAGE_NUMBER) != null
                ? Integer.parseInt(content.getRequestParameter(PARAM_PAGE_NUMBER)[0])
                : 1;
        Long popularUserId = content.getRequestParameter(PARAM_POPULAR_USER_ID) != null
                && !content.getRequestParameter(PARAM_POPULAR_USER_ID)[0].isEmpty()
                ? Long.parseLong(content.getRequestParameter(PARAM_POPULAR_USER_ID)[0])
                : null;
        Long popularTagId = content.getRequestParameter(PARAM_POPULAR_TAG_ID) != null
                && !content.getRequestParameter(PARAM_POPULAR_TAG_ID)[0].isEmpty()
                ? Long.parseLong(content.getRequestParameter(PARAM_POPULAR_TAG_ID)[0])
                : null;
        Long currentUserId = content.getSessionAttribute(PARAM_USER) != null
                ? ((User) content.getSessionAttribute(PARAM_USER)).getId()
                : null;
        long postsNumber = 0;
        try {
            if ((PARAM_DATE).equals(sortBy)) {
                posts = ServiceFactory.getPostService().findSortedByDateWorldPart(postsNumberLimit,
                        (pageNumber - 1) * postsNumberLimit, worldPart, popularUserId, popularTagId);
                postsNumber = ServiceFactory.getPostService().countPostsByWorldPart(worldPart, popularUserId, popularTagId);
            } else if ((PARAM_OWN).equals(sortBy)) {
                posts = ServiceFactory.getPostService().findSortedByDateWorldPart(postsNumberLimit,
                        (pageNumber - 1) * postsNumberLimit, worldPart, currentUserId, popularTagId);
                postsNumber = ServiceFactory.getPostService().countPostsByWorldPart(worldPart, currentUserId, popularTagId);
            } else {
                posts = ServiceFactory.getPostService().findSortedByPopularityWorldPart(postsNumberLimit,
                        (pageNumber - 1) * postsNumberLimit, worldPart);
                postsNumber = ServiceFactory.getPostService().countPostsByWorldPart(worldPart, popularUserId, popularTagId);
            }

            List<User> users = ServiceFactory.getUserService().findPopularUsersByWorldPart(usersNumberLimit, worldPart);
            List<Tag> tags = ServiceFactory.getTagService().findPopularTagsByWorldPart(tagsNumberLimit, worldPart);

            content.setRequestAttribute(PARAM_WORLD_PART, worldPart);
            content.setRequestAttribute(PARAM_PREV_PAGE_NUMBER, pageNumber - 1);
            content.setRequestAttribute(PARAM_NEXT_PAGE_NUMBER, pageNumber + 1);
            int lastPageNumber = postsNumber % postsNumberLimit == 0
                    ? (int) (postsNumber / postsNumberLimit)
                    : (int) (postsNumber / postsNumberLimit + 1);
            content.setRequestAttribute(PARAM_LAST_PAGE_NUMBER, lastPageNumber);
            content.setRequestAttribute(PARAM_POSTS, posts);
            content.setRequestAttribute(PARAM_USERS, users);
            content.setRequestAttribute(PARAM_TAGS, tags);
            content.setRequestAttribute(PARAM_SORT_BY, sortBy);
            content.setRequestAttribute(PARAM_POPULAR_TAG_ID, popularTagId);
            content.setRequestAttribute(PARAM_POPULAR_USER_ID, popularUserId);
            content.setRequestAttribute(PARAM_COMMAND, content.getRequestParameter(PARAM_COMMAND)[0]);

            page = ConfigurationManager.getProperty(PAGE_ALL_POSTS);
            content.setDirection(SessionRequestContent.Direction.FORWARD);
        } catch (ServiceException e) {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                    PARAM_COMMAND, COMMAND_OPEN_ERROR_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        }
        return page;
    }
}
