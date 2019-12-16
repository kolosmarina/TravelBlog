package by.training.kolos.service.impl;

import by.training.kolos.dao.impl.PhotoDaoImpl;
import by.training.kolos.dao.impl.TransactionPhotoDaoImpl;
import by.training.kolos.entity.Photo;
import by.training.kolos.entity.Tag;
import by.training.kolos.exception.DaoException;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.PhotoService;

import java.util.List;
import java.util.Set;

public class PhotoServiceImpl implements PhotoService {

    private static final PhotoServiceImpl INSTANCE = new PhotoServiceImpl();

    private PhotoServiceImpl() {
    }

    public static PhotoServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Photo find(Long photoId) throws ServiceException {
        try {
            return PhotoDaoImpl.getInstance().find(photoId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Photo> findPhotosWithLikesNumber(int limit, int offset, Long postId) throws ServiceException {
        try {
            return PhotoDaoImpl.getInstance().findPhotosWithLikesNumberByPostId(limit, offset, postId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Photo> findPhotosWithLikesNumber(Long postId) throws ServiceException {
        try {
            return PhotoDaoImpl.getInstance().findAllPhotosWithLikesNumber(postId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void changeMainPhoto(Long photoId) throws ServiceException {
        try {
            TransactionPhotoDaoImpl.getInstance().changeMainPhoto(photoId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public long countPhotosInPost(Long postId) throws ServiceException {
        try {
            return PhotoDaoImpl.getInstance().countPhotosInPost(postId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Photo savePhotoWithTags(Photo photo, Set<Tag> tags) throws ServiceException {
        try {
            return TransactionPhotoDaoImpl.getInstance().savePhotoWithTags(photo, tags);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
