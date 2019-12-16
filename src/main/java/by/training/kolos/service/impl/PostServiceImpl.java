package by.training.kolos.service.impl;

import by.training.kolos.dao.impl.PostDaoImpl;
import by.training.kolos.dao.impl.TransactionPostDaoImpl;
import by.training.kolos.entity.Photo;
import by.training.kolos.entity.Post;
import by.training.kolos.entity.Tag;
import by.training.kolos.exception.DaoException;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.PostService;

import java.util.List;
import java.util.Set;

public class PostServiceImpl implements PostService {

    private static final PostServiceImpl INSTANCE = new PostServiceImpl();

    private PostServiceImpl() {
    }

    public static PostServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Long postId) throws ServiceException {
        try {
            return TransactionPostDaoImpl.getInstance().delete(postId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Post find(Long postId) throws ServiceException {
        try {
            return PostDaoImpl.getInstance().find(postId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Post savePostWithPhoto(Post post, Photo photo, Set<Tag> tags) throws ServiceException {
        try {
            return TransactionPostDaoImpl.getInstance().savePostWithPhoto(post, photo, tags);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Post> findSortedByDateWorldPart(int limit, int offset, String worldPart, Long userId, Long tagId) throws ServiceException {
        try {
            if (userId == null && tagId == null) {
                return PostDaoImpl.getInstance().findSortedByDateWorldPart(limit, offset, worldPart);

            } else if (userId != null) {
                return PostDaoImpl.getInstance().findSortedByDatePostsByWorldPartAndTraveller(limit, offset, worldPart, userId);

            } else {
                return PostDaoImpl.getInstance().findSortedByDatePostsByWorldPartAndTagId(limit, offset, worldPart, tagId);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Post> findSortedByPopularityWorldPart(int limit, int offset, String worldPart) throws ServiceException {
        try {
            return PostDaoImpl.getInstance().findSortedByPopularityWorldPart(limit, offset, worldPart);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Post> findSortedByDate(int limit, int offset) throws ServiceException {
        try {
            return PostDaoImpl.getInstance().findSortedByDate(limit, offset);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public long countPostsByWorldPart(String worldPart, Long userId, Long tagId) throws ServiceException {
        try {
            if (userId == null && tagId == null) {
                return PostDaoImpl.getInstance().countPostsByWorldPart(worldPart);
            } else if (userId != null) {
                return PostDaoImpl.getInstance().countPostsByWorldPartAndUserId(worldPart, userId);
            } else {
                return PostDaoImpl.getInstance().countPostsByWorldPartAndTagId(worldPart, tagId);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public long findUserId(Long postId) throws ServiceException {
        try {
            return PostDaoImpl.getInstance().findUserId(postId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
