package by.training.kolos.dao.impl;

import by.training.kolos.connection.ConnectionPool;
import by.training.kolos.connection.ProxyConnection;
import by.training.kolos.dao.PhotoDao;
import by.training.kolos.entity.Photo;

import by.training.kolos.entity.Tag;
import by.training.kolos.exception.DaoException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TransactionPhotoDaoImpl implements PhotoDao {
    private static final Logger logger = LogManager.getLogger();

    private static final TransactionPhotoDaoImpl INSTANCE = new TransactionPhotoDaoImpl();

    private TransactionPhotoDaoImpl() {
    }

    public static TransactionPhotoDaoImpl getInstance() {
        return INSTANCE;
    }


    public Photo savePhotoWithTags(Photo photo, Set<Tag> tags) throws DaoException {
        Photo savePhoto = null;
        ProxyConnection proxyConnection = null;
        try {
            proxyConnection = ConnectionPool.getInstance().getConnection();
            proxyConnection.setAutoCommit(false);
            savePhoto = PhotoDaoImpl.getInstance().savePhoto(photo, proxyConnection);
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
                TagDaoImpl.getInstance().saveInTransactionToTagAndPhoto(savePhoto.getId(), tagId, proxyConnection);
            }
            proxyConnection.commit();
        } catch (SQLException e) {
            try {
                savePhoto = null;
                proxyConnection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Impossible rollback in method savePhotoWithTags", ex);
            }
        } finally {
            if (proxyConnection != null) {
                try {
                    proxyConnection.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "Impossible close connection in method savePhotoWithTags", e);
                }
            }
        }
        return savePhoto;
    }

    public void changeMainPhoto(Long newMainPhotoId) throws DaoException {
        ProxyConnection proxyConnection = null;
        try {
            proxyConnection = ConnectionPool.getInstance().getConnection();
            proxyConnection.setAutoCommit(false);
            PhotoDaoImpl.getInstance().changeInTransactionMainPhoto(newMainPhotoId, proxyConnection);
            PhotoDaoImpl.getInstance().setInTransactionMainPhoto(newMainPhotoId, proxyConnection);
            proxyConnection.commit();
        } catch (SQLException e) {
            try {
                proxyConnection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Impossible rollback in method changeMainPhoto", ex);
            }
        } finally {
            if (proxyConnection != null) {
                try {
                    proxyConnection.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "Impossible close connection in method changeMainPhoto", e);
                }
            }
        }
    }

    @Override
    public long countPhotosInPost(Long postId) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Photo find(Long id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Photo save(Photo entity) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
