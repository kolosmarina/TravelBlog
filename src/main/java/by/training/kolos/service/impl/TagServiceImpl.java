package by.training.kolos.service.impl;

import by.training.kolos.dao.impl.TagDaoImpl;
import by.training.kolos.entity.Tag;
import by.training.kolos.exception.DaoException;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.TagService;

import java.util.List;

public class TagServiceImpl implements TagService {

    private static final TagServiceImpl INSTANCE = new TagServiceImpl();

    private TagServiceImpl() {
    }

    public static TagServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Tag> findTagsByPhoto(Long photoId) throws ServiceException {
        try {
            return TagDaoImpl.getInstance().findAllTagsByPhoto(photoId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Tag> findPopularTagsByWorldPart(int limit, String worldPart) throws ServiceException {
        try {
            return TagDaoImpl.getInstance().findPopularTagsByWorldPart(limit, worldPart);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
