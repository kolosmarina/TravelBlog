package by.training.kolos.service.impl;

import by.training.kolos.dao.impl.LikeDaoImpl;
import by.training.kolos.entity.Like;
import by.training.kolos.exception.DaoException;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.LikeService;

public class LikeServiceImpl implements LikeService {

    private static final LikeServiceImpl INSTANCE = new LikeServiceImpl();

    private LikeServiceImpl() {
    }

    public static LikeServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Like save(Like like) throws ServiceException {
        try {
            return LikeDaoImpl.getInstance().save(like);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Like like) throws ServiceException {
        try {
            return LikeDaoImpl.getInstance().delete(like);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Like find(Long photoId, Long userId) throws ServiceException {
        try {
            return LikeDaoImpl.getInstance().find(photoId, userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public long countLikesNumber(Long photoId) throws ServiceException {
        try {
            return LikeDaoImpl.getInstance().countLikesNumber(photoId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
