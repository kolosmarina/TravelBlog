package by.training.kolos.service;

import by.training.kolos.entity.Post;
import by.training.kolos.exception.ServiceException;

public interface PostService {

    boolean delete(Long postId) throws ServiceException;

    Post find(Long postId) throws ServiceException;
}
