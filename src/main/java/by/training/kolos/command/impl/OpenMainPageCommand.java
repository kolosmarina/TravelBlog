package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.command.ApplicationConstants;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.entity.Post;
import by.training.kolos.entity.WorldPart;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Класс для выполнения запроса по получению доступа к главной странице системы
 *
 * @author Колос Марина
 */
public class OpenMainPageCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();
    /**
     * количество постов (главных фото) одновременно отображаемых на странице
     */
    private static final int postsNumberLimit = 3;
    private static final int postsNumberOffset = 0;

    /**
     * @see AbstractCommand#execute(SessionRequestContent)
     */
    @Override
    public String execute(SessionRequestContent content) {
        String page;
        try {
            List<Post> posts = ServiceFactory.getPostService().findSortedByDate(postsNumberLimit, postsNumberOffset);
            content.setRequestAttribute(ApplicationConstants.PARAM_POSTS, posts);
            content.setRequestAttribute(ApplicationConstants.PARAM_WORLD_PARTS, WorldPart.values());
            page = ConfigurationManager.getProperty(ApplicationConstants.PAGE_MAIN);
            content.setDirection(SessionRequestContent.Direction.FORWARD);
        } catch (ServiceException e) {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(ApplicationConstants.MAIN_SERVLET),
                    ApplicationConstants.PARAM_COMMAND, ApplicationConstants.COMMAND_OPEN_ERROR_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        }
        return page;
    }
}
