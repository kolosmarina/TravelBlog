package by.training.kolos.dao;

import by.training.kolos.connection.ConnectionPool;
import by.training.kolos.connection.ProxyConnection;
import by.training.kolos.entity.Entity;
import by.training.kolos.exception.DaoException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.sql.Statement;

public interface BaseDao<K, T extends Entity> {
    Logger logger = LogManager.getLogger();

    T find(K id) throws DaoException;

    T save(T entity) throws DaoException;

    boolean delete(K id) throws DaoException;

    default void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to close object Statement", e);
        }
    }

    default void close(ProxyConnection proxyConnection) {
        if (proxyConnection != null) {
            ConnectionPool.getInstance().returnConnection(proxyConnection);
        }
    }
}
