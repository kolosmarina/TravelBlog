package by.training.kolos.dao;

import by.training.kolos.entity.Comment;
import by.training.kolos.exception.DaoException;

import java.util.List;

public interface CommentDao extends BaseDao<Long, Comment> {

    List<Comment> findByPhoto(Long photoId) throws DaoException;
}
