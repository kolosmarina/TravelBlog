package by.training.kolos.dao.impl;

import by.training.kolos.connection.ConnectionPool;
import by.training.kolos.connection.ProxyConnection;
import by.training.kolos.dao.PostDao;
import by.training.kolos.entity.Photo;
import by.training.kolos.entity.Post;
import by.training.kolos.entity.Tag;
import by.training.kolos.exception.DaoException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TransactionPostDaoImpl implements PostDao {
    private static final Logger logger = LogManager.getLogger();

    private static final TransactionPostDaoImpl INSTANCE = new TransactionPostDaoImpl();

    private TransactionPostDaoImpl() {
    }

    public static TransactionPostDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Long postId) throws DaoException {
        boolean result = true;
        ProxyConnection proxyConnection = null;
        try {
            proxyConnection = ConnectionPool.getInstance().getConnection();
            proxyConnection.setAutoCommit(false);
            List<Photo> photos = PhotoDaoImpl.getInstance().findAllPhotosWithLikesNumber(postId, proxyConnection);
            for (Photo photo : photos) {
                TagDaoImpl.getInstance().deleteInTransactionTagsFromTableTagPhoto(photo.getId(), proxyConnection);
                LikeDaoImpl.getInstance().deleteLikesByPhotoId(photo.getId(), proxyConnection);
                CommentDaoImpl.getInstance().deleteByPhotoId(photo.getId(), proxyConnection);
                PhotoDaoImpl.getInstance().deletePhoto(photo.getId(), proxyConnection);
            }
            PostDaoImpl.getInstance().deleteInTransaction(postId, proxyConnection);
            proxyConnection.commit();
        } catch (SQLException e) {
            try {
                result = false;
                proxyConnection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Impossible rollback in method delete", ex);
            }
        } finally {
            if (proxyConnection != null) {
                try {
                    proxyConnection.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "Impossible close connection in method delete", e);
                }
            }
        }
        return result;
    }

    public Post savePostWithPhoto(Post post, Photo photo, Set<Tag> tags) throws DaoException {
        Post savedPost = null;
        ProxyConnection proxyConnection = null;
        try {
            proxyConnection = ConnectionPool.getInstance().getConnection();
            proxyConnection.setAutoCommit(false);
            savedPost = PostDaoImpl.getInstance().saveInTransaction(post, proxyConnection);
            photo.setPostId(savedPost.getId());
            Photo savedPhoto = PhotoDaoImpl.getInstance().savePhoto(photo, proxyConnection);
            List<Long> tagsId = new ArrayList<>();
            for (Tag tag : tags) {
                Tag existingTag = TagDaoImpl.getInstance().findInTransactionByValue(tag, proxyConnection);
                if (existingTag.getId() == null) {
                    Tag creatingTag = TagDaoImpl.getInstance().saveInTransaction(tag, proxyConnection);
                    tagsId.add(creatingTag.getId());
                } else {
                    tagsId.add(existingTag.getId());
                }
            }
            for (Long tagId : tagsId) {
                TagDaoImpl.getInstance().saveInTransactionToTagAndPhoto(savedPhoto.getId(), tagId, proxyConnection);
            }
            proxyConnection.commit();
        } catch (SQLException e) {
            try {
                savedPost = null;
                proxyConnection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Impossible rollback in method savePostWithPhoto", ex);
            }
        } finally {
            if (proxyConnection != null) {
                try {
                    proxyConnection.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "Impossible close connection in method savePostWithPhoto", e);
                }
            }
        }
        return savedPost;
    }

    @Override
    public long countPostsByWorldPart(String worldPart) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Post find(Long id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Post save(Post entity) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
