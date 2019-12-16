package by.training.kolos.dao.impl;

import by.training.kolos.connection.ConnectionPool;
import by.training.kolos.connection.ProxyConnection;
import by.training.kolos.dao.CommentDao;
import by.training.kolos.entity.Comment;
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

public class CommentDaoImpl implements CommentDao {
    private static final Logger logger = LogManager.getLogger();

    private static final String ID_COLUMN = "id";
    private static final String PUBLISH_DATE_COLUMN = "publish_date";
    private static final String VALUE_COLUMN = "value";
    private static final String USER_ID_COLUMN = "traveller_id";
    private static final String PHOTO_ID_COLUMN = "photo_id";
    private static final String NICKNAME_COLUMN = "nickname";

    private static final CommentDaoImpl INSTANCE = new CommentDaoImpl();

    private static final String SQL_FIND_ALL_COMMENTS_BY_PHOTO_ID =
            "SELECT c.id, c.publish_date, c.value, c.traveller_id, t.nickname " +
                    "FROM travelling_storage.comment c " +
                    "JOIN travelling_storage.traveller t " +
                    "ON t.id=c.traveller_id " +
                    "WHERE c.photo_id=? " +
                    "ORDER BY c.publish_date DESC";

    private static final String SQL_SAVE_COMMENT =
            "INSERT INTO travelling_storage.comment " +
                    "(publish_date, value, photo_id, traveller_id) " +
                    "VALUES (?,?,?,?)";

    private static final String SQL_DELETE_COMMENT_BY_PHOTO_ID =
            "DELETE FROM travelling_storage.comment " +
                    "WHERE photo_id=?";

    private static final String SQL_FIND_COMMENT_BY_ID =
            "SELECT id, publish_date, value, traveller_id " +
                    "FROM travelling_storage.comment " +
                    "WHERE id=?";

    private static final String SQL_DELETE_COMMENT_BY_ID =
            "DELETE FROM travelling_storage.comment " +
                    "WHERE id=?";

    private static final String SQL_DELETE_COMMENT_BY_PHOTO_ID_AND_USER_ID =
            "DELETE FROM travelling_storage.comment " +
                    "WHERE photo_id=? AND traveller_id=?";

    private CommentDaoImpl() {
    }

    public static CommentDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Comment save(Comment comment) throws DaoException {
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_SAVE_COMMENT, RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, comment.getPublishDate());
            preparedStatement.setString(2, comment.getValue());
            preparedStatement.setLong(3, comment.getPhotoId());
            preparedStatement.setLong(4, comment.getUserId());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getLong(ID_COLUMN);
                comment.setId(id);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to save comment", e);
        }
        return comment;
    }

    @Override
    public Comment find(Long commentId) throws DaoException {
        Comment comment = null;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_COMMENT_BY_ID)) {
            preparedStatement.setLong(1, commentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                comment = Comment.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .publishDate(resultSet.getLong(PUBLISH_DATE_COLUMN))
                        .value(resultSet.getString(VALUE_COLUMN))
                        .userId(resultSet.getLong(USER_ID_COLUMN))
                        .build();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find comment by id", e);
        }
        return comment;
    }

    @Override
    public boolean delete(Long commentId) throws DaoException {
        boolean result = false;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_DELETE_COMMENT_BY_ID)) {
            preparedStatement.setLong(1, commentId);
            int updateResult = preparedStatement.executeUpdate();

            if (updateResult != 0) {
                result = true;
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to delete comment", e);
        }
        return result;
    }

    @Override
    public List<Comment> findByPhoto(Long photoId) throws DaoException {
        List<Comment> comments = new ArrayList<>();
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_ALL_COMMENTS_BY_PHOTO_ID)) {
            preparedStatement.setLong(1, photoId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Comment comment = Comment.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .publishDate(resultSet.getLong(PUBLISH_DATE_COLUMN))
                        .value(resultSet.getString(VALUE_COLUMN))
                        .userId(resultSet.getLong(USER_ID_COLUMN))
                        .userNickname(resultSet.getString(NICKNAME_COLUMN))
                        .build();
                comments.add(comment);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find comments by photoId", e);
        }
        return comments;
    }

    public boolean delete(Long photoId, Long userId) throws DaoException {
        boolean result = false;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_DELETE_COMMENT_BY_PHOTO_ID_AND_USER_ID)) {
            preparedStatement.setLong(1, photoId);
            preparedStatement.setLong(2, userId);
            int updateResult = preparedStatement.executeUpdate();
            if (updateResult != 0) {
                result = true;
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to delete comment by photoId and userId", e);
        }
        return result;
    }

    //for transaction execution
    public void deleteByPhotoId(Long photoId, ProxyConnection proxyConnection) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = proxyConnection.prepareStatement(SQL_DELETE_COMMENT_BY_PHOTO_ID);
            preparedStatement.setLong(1, photoId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to delete all comments by photoId", e);
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
}
