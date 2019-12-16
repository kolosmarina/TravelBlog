package by.training.kolos.dao;

import by.training.kolos.entity.Like;
import by.training.kolos.exception.DaoException;

public interface LikeDao extends BaseDao<Long, Like> {

    Like find(Long photoId, Long userId) throws DaoException;
}
