package by.training.kolos.dao;

import by.training.kolos.connection.ProxyConnection;
import by.training.kolos.entity.Post;
import by.training.kolos.exception.DaoException;

public interface PostDao extends BaseDao<Long, Post> {

    long countPostsByWorldPart(String worldPart) throws DaoException;
}
