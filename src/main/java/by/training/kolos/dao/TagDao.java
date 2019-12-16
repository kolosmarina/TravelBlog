package by.training.kolos.dao;

import by.training.kolos.entity.Tag;
import by.training.kolos.exception.DaoException;

import java.util.List;

public interface TagDao extends BaseDao<Long, Tag> {

    List<Tag> findAllTagsByPhoto(Long photoId) throws DaoException;
}
