package by.training.kolos.dao.impl;

import by.training.kolos.connection.ConnectionPool;
import by.training.kolos.connection.ProxyConnection;
import by.training.kolos.dao.TagDao;
import by.training.kolos.entity.Tag;
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

public class TagDaoImpl implements TagDao {
    private static final Logger logger = LogManager.getLogger();

    private static final String ID_COLUMN = "id";
    private static final String VALUE_COLUMN = "value";

    private static final TagDaoImpl INSTANCE = new TagDaoImpl();

    private static final String SQL_FIND_POPULAR_TAGS_BY_WORLD_PART =
            "SELECT t.id, t.value, count(t.value) AS count FROM travelling_storage.tag AS t " +
                    "JOIN travelling_storage.tag_photo AS tp ON t.id=tp.tag_id " +
                    "JOIN travelling_storage.photo AS ph ON ph.id=tp.photo_id " +
                    "JOIN travelling_storage.post p on ph.post_id = p.id " +
                    "WHERE p.world_part=? " +
                    "GROUP BY t.value, t.id ORDER BY count DESC " +
                    "LIMIT ?";

    private static final String SQL_FIND_ALL_TAGS_BY_PHOTO_ID =
            "SELECT t.id, t.value " +
                    "FROM travelling_storage.tag_photo tph " +
                    "JOIN travelling_storage.tag t " +
                    "ON t.id=tph.tag_id " +
                    "WHERE tph.photo_id=?";

    private static final String SQL_FIND_TAG_ID_BY_VALUE_TAG =
            "SELECT id FROM travelling_storage.tag t " +
                    "WHERE t.value=?";

    private static final String SQL_SAVE_TAG =
            "INSERT INTO travelling_storage.tag (value) " +
                    "VALUES (?)";

    private static final String SQL_SAVE_TO_TAG_AND_PHOTO =
            "INSERT INTO travelling_storage.tag_photo " +
                    "(tag_id, photo_id) VALUES (?,?)";

    private static final String SQL_DELETE_FROM_TAG_AND_PHOTO =
            "DELETE FROM travelling_storage.tag_photo " +
                    "WHERE photo_id=?";


    private TagDaoImpl() {
    }

    public static TagDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Tag> findAllTagsByPhoto(Long photoId) throws DaoException {
        List<Tag> tags = new ArrayList<>();
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_ALL_TAGS_BY_PHOTO_ID)) {
            preparedStatement.setLong(1, photoId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Tag tag = Tag.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .value(resultSet.getString(VALUE_COLUMN))
                        .build();
                tags.add(tag);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find all tags by photoId", e);
        }
        return tags;
    }

    //for transaction execution
    public Tag saveInTransaction(Tag tag, ProxyConnection proxyConnection) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = proxyConnection.prepareStatement(SQL_SAVE_TAG, RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, tag.getValue());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                tag.setId(generatedKeys.getLong(ID_COLUMN));
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to save tag", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "Impossible close preparedStatement", e);
                }
            }
        }
        return tag;
    }

    public List<Tag> findPopularTagsByWorldPart(int limit, String worldPart) throws DaoException {
        List<Tag> tags = new ArrayList<>();
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_POPULAR_TAGS_BY_WORLD_PART)) {
            preparedStatement.setString(1, worldPart);
            preparedStatement.setInt(2, limit);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Tag tag = Tag.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .value(resultSet.getString(VALUE_COLUMN))
                        .build();
                tags.add(tag);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find several most popular tags", e);
        }
        return tags;
    }

    //for transaction execution
    public Tag findInTransactionByValue(Tag tag, ProxyConnection proxyConnection) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = proxyConnection.prepareStatement(SQL_FIND_TAG_ID_BY_VALUE_TAG);
            preparedStatement.setString(1, tag.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tag.setId(resultSet.getLong(ID_COLUMN));
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find tag by value", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "Impossible close preparedStatement", e);
                }
            }
        }
        return tag;
    }

    //for transaction execution
    public void saveInTransactionToTagAndPhoto(Long photoId, Long tagId, ProxyConnection proxyConnection) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = proxyConnection.prepareStatement(SQL_SAVE_TO_TAG_AND_PHOTO);
            preparedStatement.setLong(1, tagId);
            preparedStatement.setLong(2, photoId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to save tagId and photoId in table Tag and Photo", e);
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
    public void deleteInTransactionTagsFromTableTagPhoto(Long photoId, ProxyConnection proxyConnection) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = proxyConnection.prepareStatement(SQL_DELETE_FROM_TAG_AND_PHOTO);
            preparedStatement.setLong(1, photoId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to delete tagId and photoId from table Tag and Photo", e);
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
    public Tag find(Long id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Tag save(Tag entity) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
