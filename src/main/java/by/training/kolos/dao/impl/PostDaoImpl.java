package by.training.kolos.dao.impl;

import by.training.kolos.connection.ConnectionPool;
import by.training.kolos.connection.ProxyConnection;
import by.training.kolos.dao.PostDao;
import by.training.kolos.entity.Post;
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

public class PostDaoImpl implements PostDao {
    private static final Logger logger = LogManager.getLogger();

    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String PUBLISH_DATE_COLUMN = "publish_date";
    private static final String USER_ID_COLUMN = "traveller_id";
    private static final String WORLD_PART_COLUMN = "world_part";
    private static final String MAIN_PHOTO_ID_COLUMN = "main_photo_id";
    private static final String COUNT_POSTS_COLUMN = "countPosts";

    private static final PostDaoImpl INSTANCE = new PostDaoImpl();

    private static final String SQL_FIND_SORTED_BY_DATE_POSTS =
            "SELECT p.id, p.name, p.publish_date, ph.id AS main_photo_id " +
                    "FROM travelling_storage.post p " +
                    "JOIN travelling_storage.photo ph " +
                    "ON p.id = ph.post_id " +
                    "WHERE ph.is_main_photo is true ORDER BY p.publish_date " +
                    "DESC LIMIT ? OFFSET ?";

    private static final String SQL_FIND_SORTED_BY_DATE_AND_WORLD_PART_POSTS =
            "SELECT p.id, p.name, p.publish_date, p.traveller_id, ph.id AS main_photo_id " +
                    "FROM travelling_storage.post p " +
                    "JOIN travelling_storage.photo ph " +
                    "ON p.id = ph.post_id " +
                    "WHERE p.world_part=? AND ph.is_main_photo is true " +
                    "ORDER BY p.publish_date " +
                    "DESC LIMIT ? OFFSET ?";

    private static final String SQL_FIND_SORTED_BY_POPULARITY_AND_WORLD_PART_POSTS =
            "SELECT c.id, c.name, c.publish_date, c.traveller_id, mph.id AS main_photo_id FROM ( " +
                    "SELECT p.id, p.name, p.publish_date, p.traveller_id, COUNT(s.photo_id) AS count " +
                    "FROM travelling_storage.post p " +
                    "JOIN travelling_storage.photo ph " +
                    "ON p.id = ph.post_id " +
                    "LEFT JOIN travelling_storage.smile s " +
                    "ON ph.id = s.photo_id " +
                    "WHERE p.world_part=? " +
                    "GROUP BY p.id) AS c " +
                    "JOIN travelling_storage.photo mph " +
                    "ON c.id=mph.post_id " +
                    "WHERE mph.is_main_photo is true " +
                    "ORDER BY c.count DESC LIMIT ? OFFSET ?";

    private static final String SQL_COUNT_NUMBER_POSTS_BY_WORLD_PART =
            "SELECT count(id) AS countPosts " +
                    "FROM travelling_storage.post " +
                    "WHERE world_part=?";

    private static final String SQL_COUNT_NUMBER_POSTS_BY_WORLD_PART_AND_USER_ID =
            "SELECT count(id) AS countPosts " +
                    "FROM travelling_storage.post " +
                    "WHERE world_part=? AND traveller_id=?";

    private static final String SQL_COUNT_NUMBER_POSTS_BY_WORLD_PART_AND_TAG_ID =
            "SELECT count(posts.postId) countPosts FROM (SELECT  p.id postid, t.id tagId " +
                    "FROM travelling_storage.post p " +
                    "JOIN travelling_storage.photo ph on p.id = ph.post_id " +
                    "JOIN travelling_storage.tag_photo tg on ph.id=tg.photo_id " +
                    "JOIN travelling_storage.tag t ON t.id=tg.tag_id " +
                    "WHERE world_part=? AND t.id=? " +
                    "GROUP BY p.id, t.id) AS posts";

    private static final String SQL_FIND_POSTS_BY_WORLD_PART_AND_TRAVELLER =
            "SELECT p.id, p.name, p.publish_date, p.traveller_id, ph.id AS main_photo_id " +
                    "FROM travelling_storage.post p " +
                    "JOIN travelling_storage.photo ph " +
                    "ON p.id = ph.post_id " +
                    "WHERE ph.is_main_photo is true " +
                    "AND p.traveller_id=? " +
                    "AND p.world_part=? " +
                    "ORDER BY p.publish_date " +
                    "DESC LIMIT ? OFFSET ?";

    private static final String SQL_FIND_POSTS_BY_WORLD_PART_AND_TAG =
            "SELECT posts.id, posts.name, posts.publish_date,posts.traveller_id, mp.id AS main_photo_id FROM " +
                    "(SELECT p.id, p.name, p.publish_date, p.traveller_id, count(t.id) AS count, t.value " +
                    "FROM travelling_storage.post p " +
                    "JOIN travelling_storage.photo ph on p.id = ph.post_id " +
                    "JOIN travelling_storage.tag_photo tg on ph.id=tg.photo_id " +
                    "JOIN travelling_storage.tag t ON t.id=tg.tag_id " +
                    "WHERE p.world_part=? AND t.id=? " +
                    "GROUP BY p.id,t.value) AS posts " +
                    "JOIN travelling_storage.photo mp ON mp.post_id=posts.id " +
                    "WHERE mp.is_main_photo is true " +
                    "ORDER BY posts.count DESC " +
                    "LIMIT ? OFFSET ?";

    private static final String SQL_SAVE_POST =
            "INSERT INTO travelling_storage.post (name, publish_date, traveller_id, world_part) " +
                    " VALUES (?,?,?,?)";

    private static final String SQL_FIND_USER_ID_IN_POST =
            "SELECT traveller_id " +
                    "FROM travelling_storage.post " +
                    "WHERE id=?";

    private static final String SQL_DELETE_POST =
            "DELETE FROM travelling_storage.post " +
                    "WHERE id=?";

    private static final String SQL_FIND_POST_BY_ID =
            "SELECT id, name, publish_date, world_part, traveller_id " +
                    "FROM travelling_storage.post " +
                    "WHERE id=?";

    private PostDaoImpl() {
    }

    public static PostDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Post find(Long postId) throws DaoException {
        Post post = null;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_POST_BY_ID)) {
            preparedStatement.setLong(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                post = Post.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .name(resultSet.getString(NAME_COLUMN))
                        .publishDate(resultSet.getLong(PUBLISH_DATE_COLUMN))
                        .userId(resultSet.getLong(USER_ID_COLUMN))
                        .build();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find post by id", e);
        }
        return post;
    }

    @Override
    public long countPostsByWorldPart(String worldPart) throws DaoException {
        long countPosts = 0;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_COUNT_NUMBER_POSTS_BY_WORLD_PART)) {
            preparedStatement.setString(1, worldPart);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                countPosts = resultSet.getLong(COUNT_POSTS_COLUMN);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to count posts by world part", e);
        }
        return countPosts;
    }

    public long findUserId(Long postId) throws DaoException {
        long userId = 0;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_USER_ID_IN_POST)) {
            preparedStatement.setLong(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getLong(USER_ID_COLUMN);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user's id in post", e);
        }
        return userId;
    }

    //for transaction execution
    public Post saveInTransaction(Post post, ProxyConnection proxyConnection) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = proxyConnection.prepareStatement(SQL_SAVE_POST, RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, post.getName());
            preparedStatement.setLong(2, post.getPublishDate());
            preparedStatement.setLong(3, post.getUserId());
            preparedStatement.setString(4, post.getWorldPart().name());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getLong(ID_COLUMN);
                post.setId(id);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to save post", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "Impossible close preparedStatement", e);
                }
            }
        }
        return post;
    }

    //for transaction execution
    public boolean deleteInTransaction(Long postId, ProxyConnection proxyConnection) throws DaoException {
        boolean result = false;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = proxyConnection.prepareStatement(SQL_DELETE_POST);
            preparedStatement.setLong(1, postId);
            int executeUpdate = preparedStatement.executeUpdate();

            if (executeUpdate != 0) {
                result = true;
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to delete post", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "Impossible close preparedStatement", e);
                }
            }
        }
        return result;
    }

    public long countPostsByWorldPartAndUserId(String worldPart, Long userId) throws DaoException {
        long countPosts = 0;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_COUNT_NUMBER_POSTS_BY_WORLD_PART_AND_USER_ID)) {
            preparedStatement.setString(1, worldPart);
            preparedStatement.setLong(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                countPosts = resultSet.getLong(COUNT_POSTS_COLUMN);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to count posts by world part and userId", e);
        }
        return countPosts;
    }


    public long countPostsByWorldPartAndTagId(String worldPart, Long tagId) throws DaoException {
        long countPosts = 0;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_COUNT_NUMBER_POSTS_BY_WORLD_PART_AND_TAG_ID)) {
            preparedStatement.setString(1, worldPart);
            preparedStatement.setLong(2, tagId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                countPosts = resultSet.getLong(COUNT_POSTS_COLUMN);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to count posts by world part and tag", e);
        }
        return countPosts;
    }

    public List<Post> findSortedByDate(int limit, int offset) throws DaoException {
        List<Post> posts = new ArrayList<>();
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_SORTED_BY_DATE_POSTS)) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Post post = Post.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .name(resultSet.getString(NAME_COLUMN))
                        .publishDate(resultSet.getLong(PUBLISH_DATE_COLUMN))
                        .mainPhotoId(resultSet.getLong(MAIN_PHOTO_ID_COLUMN))
                        .build();
                posts.add(post);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find several posts sorted by date", e);
        }
        return posts;
    }

    public List<Post> findSortedByDateWorldPart(int limit, int offset, String worldPart) throws DaoException {
        List<Post> posts = new ArrayList<>();
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_SORTED_BY_DATE_AND_WORLD_PART_POSTS)) {
            preparedStatement.setString(1, worldPart);
            preparedStatement.setInt(2, limit);
            preparedStatement.setInt(3, offset);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Post post = Post.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .name(resultSet.getString(NAME_COLUMN))
                        .publishDate(resultSet.getLong(PUBLISH_DATE_COLUMN))
                        .mainPhotoId(resultSet.getLong(MAIN_PHOTO_ID_COLUMN))
                        .userId(resultSet.getLong(USER_ID_COLUMN))
                        .build();
                posts.add(post);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find several posts sorted by date and world part", e);
        }
        return posts;
    }

    public List<Post> findSortedByPopularityWorldPart(int limit, int offset, String worldPart) throws DaoException {
        List<Post> posts = new ArrayList<>();
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_SORTED_BY_POPULARITY_AND_WORLD_PART_POSTS)) {
            preparedStatement.setString(1, worldPart);
            preparedStatement.setInt(2, limit);
            preparedStatement.setInt(3, offset);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Post post = Post.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .name(resultSet.getString(NAME_COLUMN))
                        .publishDate(resultSet.getLong(PUBLISH_DATE_COLUMN))
                        .mainPhotoId(resultSet.getLong(MAIN_PHOTO_ID_COLUMN))
                        .userId(resultSet.getLong(USER_ID_COLUMN))
                        .build();
                posts.add(post);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find several posts sorted by popularity and world part", e);
        }
        return posts;
    }

    public List<Post> findSortedByDatePostsByWorldPartAndTraveller(int limit, int offset, String worldPart, Long travellerId) throws DaoException {
        List<Post> posts = new ArrayList<>();
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_POSTS_BY_WORLD_PART_AND_TRAVELLER)) {
            preparedStatement.setLong(1, travellerId);
            preparedStatement.setString(2, worldPart);
            preparedStatement.setInt(3, limit);
            preparedStatement.setInt(4, offset);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Post post = Post.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .name(resultSet.getString(NAME_COLUMN))
                        .publishDate(resultSet.getLong(PUBLISH_DATE_COLUMN))
                        .mainPhotoId(resultSet.getLong(MAIN_PHOTO_ID_COLUMN))
                        .userId(resultSet.getLong(USER_ID_COLUMN))
                        .build();
                posts.add(post);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find several posts sorted by date and traveller", e);
        }
        return posts;
    }

    public List<Post> findSortedByDatePostsByWorldPartAndTagId(int limit, int offset, String worldPart, Long tagId) throws DaoException {
        List<Post> posts = new ArrayList<>();
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_POSTS_BY_WORLD_PART_AND_TAG)) {
            preparedStatement.setString(1, worldPart);
            preparedStatement.setLong(2, tagId);
            preparedStatement.setInt(3, limit);
            preparedStatement.setInt(4, offset);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Post post = Post.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .name(resultSet.getString(NAME_COLUMN))
                        .publishDate(resultSet.getLong(PUBLISH_DATE_COLUMN))
                        .mainPhotoId(resultSet.getLong(MAIN_PHOTO_ID_COLUMN))
                        .userId(resultSet.getLong(USER_ID_COLUMN))
                        .build();
                posts.add(post);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find several posts sorted by date and tag", e);
        }
        return posts;
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Post save(Post entity) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
