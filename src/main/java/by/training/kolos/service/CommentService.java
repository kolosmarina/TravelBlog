package by.training.kolos.service;

import by.training.kolos.entity.Comment;
import by.training.kolos.exception.ServiceException;

public interface CommentService {

    Comment save(Comment comment) throws ServiceException;

    Comment find(Long commentId) throws ServiceException;

    boolean delete(Long commentId) throws ServiceException;
}
