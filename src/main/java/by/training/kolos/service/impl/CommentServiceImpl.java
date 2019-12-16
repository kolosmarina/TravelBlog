package by.training.kolos.service.impl;

import by.training.kolos.dao.impl.CommentDaoImpl;
import by.training.kolos.entity.Comment;
import by.training.kolos.exception.DaoException;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.CommentService;

import java.util.List;

public class CommentServiceImpl implements CommentService {

    private static final CommentServiceImpl INSTANCE = new CommentServiceImpl();

    private CommentServiceImpl() {
    }

    public static CommentServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Comment save(Comment comment) throws ServiceException {
        try {
            return CommentDaoImpl.getInstance().save(comment);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Long commentId) throws ServiceException {
        try {
            return CommentDaoImpl.getInstance().delete(commentId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Comment find(Long commentId) throws ServiceException {
        try {
            return CommentDaoImpl.getInstance().find(commentId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Comment> findByPhoto(Long photoId) throws ServiceException {
        try {
            return CommentDaoImpl.getInstance().findByPhoto(photoId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
