package by.training.kolos.dao;

import by.training.kolos.entity.Photo;
import by.training.kolos.exception.DaoException;

public interface PhotoDao extends BaseDao<Long, Photo> {

    long countPhotosInPost(Long postId) throws DaoException;
}
