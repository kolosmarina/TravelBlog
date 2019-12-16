package by.training.kolos.service;

import by.training.kolos.service.impl.*;

public final class ServiceFactory {

    private ServiceFactory() {
    }

    public static UserServiceImpl getUserService() {
        return UserServiceImpl.getInstance();
    }

    public static PostServiceImpl getPostService() {
        return PostServiceImpl.getInstance();
    }

    public static PhotoServiceImpl getPhotoService() {
        return PhotoServiceImpl.getInstance();
    }

    public static CommentServiceImpl getCommentService() {
        return CommentServiceImpl.getInstance();
    }

    public static TagServiceImpl getTagService() {
        return TagServiceImpl.getInstance();
    }

    public static LikeServiceImpl getLikeService() {
        return LikeServiceImpl.getInstance();
    }
}
