package by.training.kolos.dao.impl;

import by.training.kolos.connection.ConnectionPool;
import by.training.kolos.connection.ProxyConnection;
import by.training.kolos.dao.PhotoDao;
import by.training.kolos.entity.Photo;
import by.training.kolos.exception.DaoException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class PhotoDaoImpl implements PhotoDao {
    private static final Logger logger = LogManager.getLogger();

    private static final String ID_COLUMN = "id";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String POST_ID_COLUMN = "post_id";
    private static final String IS_MAIN_PHOTO_COLUMN = "is_main_photo";
    private static final String URL_COLUMN = "url";
    private static final String COUNT_COLUMN = "count";

    private static final PhotoDaoImpl INSTANCE = new PhotoDaoImpl();

    private static final String SQL_FIND_SEVERAL_PHOTOS_WITH_LIKES_BY_POST =
            "SELECT ph.id, ph.description, count(s.photo_id) AS count " +
                    "FROM travelling_storage.photo ph " +
                    "LEFT JOIN travelling_storage.smile s " +
                    "ON ph.id=s.photo_id " +
                    "WHERE ph.post_id=? " +
                    "GROUP BY s.photo_id, ph.id " +
                    "LIMIT ? OFFSET ?";

    private static final String SQL_FIND_ALL_PHOTOS_WITH_LIKES_IN_POST =
            "SELECT ph.id, ph.description, count(s.photo_id) AS count " +
                    "FROM travelling_storage.photo ph " +
                    "LEFT JOIN travelling_storage.smile s " +
                    "ON ph.id=s.photo_id " +
                    "WHERE ph.post_id=? " +
                    "GROUP BY s.photo_id, ph.id " +
                    "ORDER BY ph.id";

    private static final String SQL_COUNT_PHOTOS_IN_POST =
            "SELECT count(id) AS count " +
                    "FROM travelling_storage.photo " +
                    "WHERE post_id=?";

    private static final String SQL_FIND_PHOTO_BY_ID =
            "SELECT ph.id, ph.url, ph.description, ph.post_id, count (s.photo_id) AS count " +
                    "FROM travelling_storage.photo ph " +
                    "LEFT JOIN travelling_storage.smile s " +
                    "ON ph.id = s.photo_id " +
                    "WHERE ph.id=? " +
                    "GROUP BY s.photo_id, ph.id";

    private static final String SQL_SAVE_PHOTO =
            "INSERT INTO travelling_storage.photo (post_id, description, " +
                    "is_main_photo, url) VALUES (?,?,?,?)";

    private static final String SQL_DELETE_PHOTO =
            "DELETE FROM travelling_storage.photo " +
                    "WHERE id=?";

    private static final String SQL_SET_MAIN_PHOTO_IN_POST =
            "UPDATE travelling_storage.photo " +
                    "SET is_main_photo=true " +
                    "WHERE id=?";

    private static final String SQL_CHANGE_MAIN_PHOTO_IN_POST =
            "UPDATE travelling_storage.photo " +
                    "SET is_main_photo = false " +
                    "WHERE id=(" +
                    "SELECT id FROM travelling_storage.photo " +
                    "WHERE post_id=(" +
                    "SELECT p.post_id FROM travelling_storage.photo p " +
                    "WHERE p.id=?) " +
                    "AND is_main_photo=true)";

    private PhotoDaoImpl() {
    }

    public static PhotoDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Photo find(Long photoId) throws DaoException {
        Photo photo = null;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_PHOTO_BY_ID)) {
            preparedStatement.setLong(1, photoId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                photo = Photo.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .description(resultSet.getString(DESCRIPTION_COLUMN))
                        .tags(resultSet.getLong(COUNT_COLUMN))
                        .url(resultSet.getString(URL_COLUMN))
                        .postId(Long.valueOf(resultSet.getString(POST_ID_COLUMN)))
                        .build();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find photo", e);
        }
        return photo;
    }

    @Override
    public long countPhotosInPost(Long postId) throws DaoException {
        long photosCount = 0;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_COUNT_PHOTOS_IN_POST)) {
            preparedStatement.setLong(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                photosCount = resultSet.getLong(COUNT_COLUMN);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to count photos in post", e);
        }
        return photosCount;
    }

    public List<Photo> findPhotosWithLikesNumberByPostId(int limit, int offset, Long postId) throws DaoException {
        List<Photo> photos = new ArrayList<>();
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_SEVERAL_PHOTOS_WITH_LIKES_BY_POST)) {
            preparedStatement.setLong(1, postId);
            preparedStatement.setInt(2, limit);
            preparedStatement.setInt(3, offset);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Photo photo = Photo.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .description(resultSet.getString(DESCRIPTION_COLUMN))
                        .tags(resultSet.getLong(COUNT_COLUMN))
                        .build();
                photos.add(photo);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find several photos in post with number of likes", e);
        }
        return photos;
    }

    //for transaction execution
    public List<Photo> findAllPhotosWithLikesNumber(Long postId, ProxyConnection proxyConnection) throws DaoException {
        List<Photo> photos = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = proxyConnection.prepareStatement(SQL_FIND_ALL_PHOTOS_WITH_LIKES_IN_POST);
            preparedStatement.setLong(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Photo photo = Photo.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .description(resultSet.getString(DESCRIPTION_COLUMN))
                        .tags(resultSet.getLong(COUNT_COLUMN))
                        .build();
                photos.add(photo);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find all photos in post with number of likes", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "Impossible close preparedStatement", e);
                }
            }
        }
        return photos;
    }

    public List<Photo> findAllPhotosWithLikesNumber(Long postId) throws DaoException {
        List<Photo> photos = new ArrayList<>();
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_ALL_PHOTOS_WITH_LIKES_IN_POST)) {
            preparedStatement.setLong(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Photo photo = Photo.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .description(resultSet.getString(DESCRIPTION_COLUMN))
                        .tags(resultSet.getLong(COUNT_COLUMN))
                        .build();
                photos.add(photo);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find all photos in post with number of likes", e);
        }
        return photos;
    }

    //for transaction execution
    public Photo savePhoto(Photo photo, ProxyConnection proxyConnection) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = proxyConnection.prepareStatement(SQL_SAVE_PHOTO, RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, photo.getPostId());
            preparedStatement.setString(2, photo.getDescription());
            preparedStatement.setBoolean(3, photo.isMainPhoto());
            preparedStatement.setString(4, photo.getUrl());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                photo.setId(generatedKeys.getLong(ID_COLUMN));
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to save photo in post", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "Impossible close preparedStatement", e);
                }
            }
        }
        return photo;
    }

    //for transaction execution
    public void deletePhoto(Long photoId, ProxyConnection proxyConnection) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = proxyConnection.prepareStatement(SQL_DELETE_PHOTO);
            preparedStatement.setLong(1, photoId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to delete photo in post", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "Impossible close preparedStatement", e);
                }
            }
        }
    }

    //for transaction execution
    public void setInTransactionMainPhoto(Long photoId, ProxyConnection proxyConnection) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = proxyConnection.prepareStatement(SQL_SET_MAIN_PHOTO_IN_POST);
            preparedStatement.setLong(1, photoId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to set main photo in post", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "Impossible close preparedStatement", e);
                }
            }
        }
    }

    //for transaction execution
    public void changeInTransactionMainPhoto(Long photoId, ProxyConnection proxyConnection) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = proxyConnection.prepareStatement(SQL_CHANGE_MAIN_PHOTO_IN_POST);
            preparedStatement.setLong(1, photoId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to change main photo in post", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "Impossible close preparedStatement", e);
                }
            }
        }
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
