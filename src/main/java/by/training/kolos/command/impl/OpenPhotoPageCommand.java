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

public class OpenPhotoPageCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(SessionRequestContent content) {
        String page;
        Long photoId = Long.parseLong(content.getRequestParameter(PARAM_PHOTO_ID)[0]);
        User user = (User) content.getSessionAttribute(PARAM_USER);
        try {
            Photo photo = ServiceFactory.getPhotoService().find(photoId);
            List<Photo> photos = ServiceFactory.getPhotoService().findPhotosWithLikesNumber(photo.getPostId());
            List<Tag> tags = ServiceFactory.getTagService().findTagsByPhoto(photoId);
            List<Comment> comments = ServiceFactory.getCommentService().findByPhoto(photoId);
            long likesNumber = ServiceFactory.getLikeService().countLikesNumber(photoId);

            Like like = null;
            if (user != null) {
                like = ServiceFactory.getLikeService().find(photoId, user.getId());
            }
            positionPhoto(content, photoId, photos);
            content.setRequestAttribute(PARAM_PHOTO, photo);
            content.setRequestAttribute(PARAM_LIKE, like);
            content.setRequestAttribute(PARAM_LIKES_NUMBER, likesNumber);
            content.setRequestAttribute(PARAM_TAGS, tags);
            content.setRequestAttribute(PARAM_COMMENTS, comments);
            page = ConfigurationManager.getProperty(PAGE_PHOTO);
            content.setDirection(SessionRequestContent.Direction.FORWARD);
        } catch (ServiceException e) {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                    PARAM_COMMAND, COMMAND_OPEN_ERROR_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        }
        return page;
    }

    private void positionPhoto(SessionRequestContent content, Long photoId, List<Photo> photos) {
        int currentIndex = -1;
        int previousIndex = -1;
        int nextIndex = -1;
        for (int i = 0; i < photos.size(); i++) {
            if (photoId.equals(photos.get(i).getId())) {
                currentIndex = i;
                if (currentIndex != 0) {
                    previousIndex = currentIndex - 1;
                }
                if (currentIndex != photos.size() - 1) {
                    nextIndex = currentIndex + 1;
                }
                break;
            }
        }
        if (previousIndex != -1) {
            Long prevPhotoId = photos.get(previousIndex).getId();
            content.setRequestAttribute(PARAM_PREV_PHOTO_ID, prevPhotoId);
        }
        if (nextIndex != -1) {
            Long nextPhotoId = photos.get(nextIndex).getId();
            content.setRequestAttribute(PARAM_NEXT_PHOTO_ID, nextPhotoId);
        }
        boolean hasNextPhoto = nextIndex != -1;
        boolean hasPrevPhoto = previousIndex != -1;
        content.setRequestAttribute(PARAM_HAS_NEXT_PHOTO, hasNextPhoto);
        content.setRequestAttribute(PARAM_HAS_PREV_PHOTO, hasPrevPhoto);
    }
}
