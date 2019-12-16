package by.training.kolos.dao.impl;

import by.training.kolos.connection.ConnectionPool;
import by.training.kolos.connection.ProxyConnection;
import by.training.kolos.dao.LikeDao;
import by.training.kolos.entity.Like;
import by.training.kolos.exception.DaoException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeDaoImpl implements LikeDao {
    private static final Logger logger = LogManager.getLogger();

    private static final String PHOTO_ID_COLUMN = "photo_id";
    private static final String USER_ID_COLUMN = "traveller_id";
    private static final String COUNT_COLUMN = "count";

    private static final LikeDaoImpl INSTANCE = new LikeDaoImpl();

    private static final String SQL_FIND_LIKE =
            "SELECT s.photo_id, s.traveller_id " +
                    "FROM travelling_storage.smile s " +
                    "WHERE s.photo_id=? AND traveller_id=?";

    private static final String SQL_SAVE_LIKE =
            "INSERT INTO travelling_storage.smile " +
                    "(photo_id, traveller_id) " +
                    "VALUES (?,?)";

    private static final String SQL_DELETE_LIKE =
            "DELETE FROM travelling_storage.smile " +
                    "WHERE photo_id=? AND traveller_id=?";

    private static final String SQL_COUNT_LIKES_NUMBER =
            "SELECT count(s.photo_id) AS count " +
                    "FROM travelling_storage.smile s " +
                    "WHERE s.photo_id=? " +
                    "GROUP BY s.photo_id";

    private static final String SQL_DELETE_LIKES_BY_PHOTO_ID =
            "DELETE FROM travelling_storage.smile " +
                    "WHERE photo_id=?";

    private LikeDaoImpl() {
    }

    public static LikeDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Like find(Long photoId, Long userId) throws DaoException {
        Like like = null;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_LIKE)) {
            preparedStatement.setLong(1, photoId);
            preparedStatement.setLong(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                like = Like.builder()
                        .photoId(resultSet.getLong(PHOTO_ID_COLUMN))
                        .userId(resultSet.getLong(USER_ID_COLUMN))
                        .build();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find like", e);
        }
        return like;
    }

    @Override
    public Like save(Like like) throws DaoException {
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_SAVE_LIKE)) {
            preparedStatement.setLong(1, like.getPhotoId());
            preparedStatement.setLong(2, like.getUserId());
            int updateResult = preparedStatement.executeUpdate();

            if (updateResult == 0) {
                like = null;
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to save like", e);
        }
        return like;
    }

    public boolean delete(Like like) throws DaoException {
        boolean result = false;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_DELETE_LIKE)) {
            preparedStatement.setLong(1, like.getPhotoId());
            preparedStatement.setLong(2, like.getUserId());
            int executeUpdate = preparedStatement.executeUpdate();

            if (executeUpdate != 0) {
                result = true;
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to delete like", e);
        }
        return result;
    }

    //for transaction execution
    public void deleteLikesByPhotoId(Long photoId, ProxyConnection proxyConnection) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = proxyConnection.prepareStatement(SQL_DELETE_LIKES_BY_PHOTO_ID);
            preparedStatement.setLong(1, photoId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to delete like in deleting photo", e);
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

    public long countLikesNumber(Long photoId) throws DaoException {
        long likesNumber = 0;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_COUNT_LIKES_NUMBER)) {
            preparedStatement.setLong(1, photoId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                likesNumber = resultSet.getLong(COUNT_COLUMN);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to count likes", e);
        }
        return likesNumber;
    }

    @Override
    public Like find(Long id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
