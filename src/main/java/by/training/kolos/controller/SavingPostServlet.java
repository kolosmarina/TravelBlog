package by.training.kolos.controller;

import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.config.MessageManager;
import by.training.kolos.connection.ConnectionPool;
import by.training.kolos.entity.*;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static by.training.kolos.command.ApplicationConstants.*;


@WebServlet("/upload")
@MultipartConfig
public class SavingPostServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute(PARAM_USER);
        String worldPart = req.getParameter(PARAM_WORLD_PART);
        int photosNumberAvailableForUpload = Integer.parseInt(req.getParameter(PARAM_PHOTOS_NUMBER_AVAILABLE_FOR_UPLOAD));

        if (user == null) {
            handleUserNotAuthenticated(req, resp);
            return;
        }
        Long postId = req.getParameter(PARAM_POST_ID) != null
                ? Long.parseLong(req.getParameter(PARAM_POST_ID))
                : null;
        String postName = req.getParameter(PARAM_POST_NAME) != null
                ? req.getParameter(PARAM_POST_NAME)
                .replaceAll(JS_TAG, EMPTY_STRING).trim()
                : null;

        if ((postId == null && postName != null) && postName.length() == 0) {
            handleEmptyPostName(req, resp, worldPart);
            return;
        }

        List<Part> photoParts = req.getParts().stream().filter(it -> it.getName()
                .startsWith(PARAM_PHOTO + SEPARATOR_FOR_PARAMS))
                .filter(it -> it.getSize() != 0).collect(Collectors.toList());
        if (photoParts.size() == 0) {
            handleNoPhotosUploaded(req, resp, postId, worldPart);
            return;
        } else {
            for (int i = 1; i <= photosNumberAvailableForUpload; i++) {
                Part photoPart = req.getPart(PARAM_PHOTO + SEPARATOR_FOR_PARAMS + i);
                if (photoPart.getSize() != 0) {
                    String absolutePath = saveToFile(user, photoPart);
                    String description = req.getParameter(PARAM_DESCRIPTION + SEPARATOR_FOR_PARAMS + i)
                            .replaceAll(JS_TAG, EMPTY_STRING).trim();
                    Post post = null;
                    if (postId == null) {
                        post = Post.builder()
                                .worldPart(WorldPart.valueOf(worldPart))
                                .name(postName)
                                .publishDate(Instant.now().toEpochMilli())
                                .userId(user.getId())
                                .build();
                    }
                    Photo photo = Photo.builder()
                            .description(description)
                            .url(absolutePath)
                            .build();

                    String tagsLine = req.getParameter(NAME_FOR_TAGS + i)
                            .toLowerCase().replaceAll(JS_TAG, EMPTY_STRING);
                    Set<Tag> tags = Arrays.stream(tagsLine.split(SEPARATOR_FOR_TAGS))
                            .map(String::trim)
                            .filter(it -> it.length() != 0)
                            .map(it -> Tag.builder().value(it).build())
                            .collect(Collectors.toSet());
                    try {
                        if (postId == null) {
                            photo.setIsMainPhoto(true);
                            Post savePost = ServiceFactory.getPostService().savePostWithPhoto(post, photo, tags);
                            postId = savePost.getId();
                            logger.log(Level.DEBUG, "Post-{} has been saved with first photo", postId);
                        } else {
                            photo.setPostId(postId);
                            ServiceFactory.getPhotoService().savePhotoWithTags(photo, tags);
                            logger.log(Level.DEBUG, "New photo has been saved to post-{}", postId);
                        }
                    } catch (ServiceException e) {
                        String page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                                PARAM_COMMAND, COMMAND_OPEN_ERROR_PAGE);
                        resp.sendRedirect(req.getContextPath() + page);
                    }
                }
            }
        }

        String page = String.format("%s?%s=%s&%s=%s&%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                PARAM_COMMAND, COMMAND_OPEN_NEW_POST_PAGE, PARAM_WORLD_PART, worldPart, PARAM_POST_ID, postId);
        logger.log(Level.DEBUG, "First part of the post was saved, continue uploading photos");
        resp.sendRedirect(req.getContextPath() + page);
    }

    private String saveToFile(User user, Part photoPart) throws IOException {
        File file = new File(ConnectionPool.getInstance()
                .getProperties().getProperty(PHOTO_STORAGE) + user.getId() + UNDERSCORE + new Date().getTime() + ".jpg");
        try (InputStream photoFromBrowser = photoPart.getInputStream();
             FileOutputStream photoToFile = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = photoFromBrowser.read(buffer)) != -1) {
                photoToFile.write(buffer, 0, len);
            }
        }
        return file.getAbsolutePath();
    }

    private void handleUserNotAuthenticated(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.log(Level.DEBUG, "User does not have access to create a post");
        String page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                PARAM_COMMAND, COMMAND_OPEN_MAIN_PAGE);
        resp.sendRedirect(req.getContextPath() + page);
    }

    private void handleEmptyPostName(HttpServletRequest req, HttpServletResponse resp, String worldPart) throws IOException {
        logger.log(Level.DEBUG, "PostName is empty");
        req.setAttribute(PARAM_EMPTY_POST_NAME, MessageManager.getProperty(MESSAGE_EMPTY_POST_NAME));
        String page = String.format("%s?%s=%s&%s=%s&%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                PARAM_COMMAND, COMMAND_OPEN_NEW_POST_PAGE, PARAM_WORLD_PART, worldPart,
                PARAM_EMPTY_POST_NAME, MessageManager.getProperty(MESSAGE_EMPTY_POST_NAME));
        resp.sendRedirect(req.getContextPath() + page);
    }

    private void handleNoPhotosUploaded(HttpServletRequest req, HttpServletResponse resp, Long postId, String worldPart) throws IOException {
        logger.log(Level.DEBUG, "No files to download");
        String page;
        if (postId == null) {
            page = String.format("%s?%s=%s&%s=%s&%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET), PARAM_COMMAND,
                    COMMAND_OPEN_NEW_POST_PAGE, PARAM_WORLD_PART, worldPart, PARAM_EMPTY_FILES,
                    MessageManager.getProperty(MESSAGE_EMPTY_FILES));
        } else {
            page = String.format("%s?%s=%s&%s=%s&%s=%s&%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                    PARAM_COMMAND, COMMAND_OPEN_NEW_POST_PAGE, PARAM_WORLD_PART, worldPart, PARAM_POST_ID, postId,
                    PARAM_EMPTY_FILES, MessageManager.getProperty(MESSAGE_EMPTY_FILES));
        }
        resp.sendRedirect(req.getContextPath() + page);
    }
}
