package by.training.kolos.dao.impl;

import by.training.kolos.connection.ConnectionPool;
import by.training.kolos.connection.ProxyConnection;
import by.training.kolos.dao.UserDao;
import by.training.kolos.entity.UserRole;
import by.training.kolos.entity.User;
import by.training.kolos.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger();

    private static final String ID_COLUMN = "id";
    private static final String USER_ROLE_COLUMN = "role";
    private static final String EMAIL_COLUMN = "email";
    private static final String PASSWORD_COLUMN = "password";
    private static final String NICKNAME_COLUMN = "nickname";
    private static final String REGISTRATION_DATE_COLUMN = "registration_date";
    private static final String IS_ACTIVE_COLUMN = "is_active";
    private static final String COUNT_USERS_COLUMN = "countUsers";

    private static final UserDaoImpl INSTANCE = new UserDaoImpl();

    private static final String SQL_FIND_ALL_USERS =
            "SELECT id, nickname, role, is_active " +
                    "FROM travelling_storage.traveller " +
                    "ORDER BY id";

    private static final String SQL_FIND_USER_BY_LOGIN_AND_PASSWORD =
            "SELECT id, role, nickname, is_active " +
                    "FROM travelling_storage.traveller " +
                    "WHERE email=? AND password=?";

    private static final String SQL_CHANGE_USER_STATUS =
            "UPDATE travelling_storage.traveller " +
                    "SET is_active=? " +
                    "WHERE id=?";

    private static final String SQL_FIND_POPULAR_USERS_BY_WORLD_PART =
            "SELECT sum_like.id, sum_like.nickname,SUM(sum_like.count) AS count_like FROM ( " +
                    "SELECT t.id, t.nickname, p.name, count(s.photo_id) AS count " +
                    "FROM travelling_storage.traveller AS t " +
                    "JOIN travelling_storage.post p " +
                    "ON t.id = p.traveller_id " +
                    "JOIN travelling_storage.photo ph " +
                    "ON p.id = ph.post_id " +
                    "JOIN travelling_storage.smile s " +
                    "ON ph.id = s.photo_id " +
                    "WHERE world_part=? " +
                    "GROUP BY p.id, t.id) AS sum_like " +
                    "GROUP BY sum_like.id, sum_like.nickname " +
                    "ORDER BY count_like DESC LIMIT ?; ";

    private static final String SQL_SAVE_USER =
            "INSERT INTO travelling_storage.traveller " +
                    "(role, email, password, nickname, registration_date, is_active) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_STATUS_USER =
            "SELECT is_active " +
                    "FROM travelling_storage.traveller " +
                    "WHERE id=?";

    private static final String SQL_COUNT_USERS_BY_EMAIL =
            "SELECT count(id) AS countUsers " +
                    "FROM travelling_storage.traveller " +
                    "WHERE email=?";

    private static final String SQL_COUNT_USERS_BY_NICKNAME =
            "SELECT count(id) AS countUsers " +
                    "FROM travelling_storage.traveller " +
                    "WHERE UPPER (nickname) like UPPER (?)";

    private UserDaoImpl() {
    }

    public static UserDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public User save(User user) throws DaoException {
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_SAVE_USER, RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUserRole().name());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getNickname());
            preparedStatement.setLong(5, user.getRegistrationDate());
            preparedStatement.setBoolean(6, user.getIsActive());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getLong(ID_COLUMN);
                user.setId(id);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to save user", e);
        }
        return user;
    }

    @Override
    public User findUseByEmailAndPassword(String email, String password) throws DaoException {
        User user = null;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_USER_BY_LOGIN_AND_PASSWORD)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = User.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .role(UserRole.valueOf(resultSet.getString(USER_ROLE_COLUMN).toUpperCase()))
                        .nickname(resultSet.getString(NICKNAME_COLUMN))
                        .isActive(resultSet.getBoolean(IS_ACTIVE_COLUMN))
                        .build();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by email and password", e);
        }
        return user;
    }

    public List<User> findAll() throws DaoException {
        List<User> users = new ArrayList<>();
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             Statement statement = proxyConnection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_USERS);

            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .role(UserRole.valueOf(resultSet.getString(USER_ROLE_COLUMN).toUpperCase()))
                        .nickname(resultSet.getString(NICKNAME_COLUMN))
                        .isActive(resultSet.getBoolean(IS_ACTIVE_COLUMN))
                        .build();
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find all users", e);
        }
        return users;
    }

    public void changeStatus(Long userId, Boolean isActive) throws DaoException {
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_CHANGE_USER_STATUS)) {
            preparedStatement.setBoolean(1, isActive);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to change user's status", e);
        }
    }

    public List<User> findPopularUsersByWorldPart(int limit, String worldPart) throws DaoException {
        List<User> users = new ArrayList<>();
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_POPULAR_USERS_BY_WORLD_PART)) {
            preparedStatement.setString(1, worldPart);
            preparedStatement.setInt(2, limit);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getLong(ID_COLUMN))
                        .nickname(resultSet.getString(NICKNAME_COLUMN))
                        .build();
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find several most popular user", e);
        }
        return users;
    }

    public boolean findUserStatus(Long userId) throws DaoException {
        boolean result = false;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_FIND_STATUS_USER)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getBoolean(IS_ACTIVE_COLUMN);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user's status", e);
        }
        return result;
    }

    public long countUsersByEmail(String email) throws DaoException {
        long usersNumber = 0;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_COUNT_USERS_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                usersNumber = resultSet.getLong(COUNT_USERS_COLUMN);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to count users by email", e);
        }
        return usersNumber;
    }

    public long countUsersByNickname(String nickname) throws DaoException {
        long usersNumber = 0;
        try (ProxyConnection proxyConnection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = proxyConnection.prepareStatement(SQL_COUNT_USERS_BY_NICKNAME)) {
            preparedStatement.setString(1, nickname);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                usersNumber = resultSet.getLong(COUNT_USERS_COLUMN);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to count users by nickname", e);
        }
        return usersNumber;
    }

    @Override
    public User find(Long id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
