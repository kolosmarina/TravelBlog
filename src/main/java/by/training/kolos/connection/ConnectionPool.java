package by.training.kolos.connection;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import by.training.kolos.command.ApplicationConstants;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс для создания пула соединений с базой данных
 *
 * @author Колос Марина
 */
public final class ConnectionPool {
    private static final Logger logger = LogManager.getLogger();

    private static ConnectionPool instance;
    private static final ReentrantLock poolLock = new ReentrantLock();
    private static final ReadWriteLock accessLock = new ReentrantReadWriteLock();
    private static AtomicBoolean instanceCreated = new AtomicBoolean();
    private final BlockingQueue<ProxyConnection> freeConnections;
    private final Queue<ProxyConnection> givenAwayConnections;
    private final int DEFAULT_POOL_SIZE;
    private final Properties properties;
    //for testing connections to a test database
    private static boolean isTest;

    private ConnectionPool() {
        if (null != instance) {
            throw new RuntimeException("Attempt to create a singleton copy");
        }
    }

    {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            logger.log(Level.DEBUG, "Driver was  initialize in connection pool");
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error initializing connection pool", e);
        }
        PropertyLoader propertyLoader = new PropertyLoader();
        properties = isTest
                ? propertyLoader.loadFile("applicationTest.properties")
                : propertyLoader.loadFile("application.properties");
        DEFAULT_POOL_SIZE = Integer.parseInt(properties.getProperty(ApplicationConstants.SIZE_POOL));
        freeConnections = new LinkedBlockingDeque<>(DEFAULT_POOL_SIZE);
        givenAwayConnections = new ArrayDeque<>();
        while (freeConnections.size() < DEFAULT_POOL_SIZE) {
            ProxyConnection proxyConnection = null;
            try {
                proxyConnection = new ProxyConnection(DriverManager.getConnection(properties.getProperty(ApplicationConstants.URL_KEY), properties.getProperty(ApplicationConstants.USER_KEY),
                        properties.getProperty(ApplicationConstants.PASSWORD_KEY)));
                freeConnections.offer(proxyConnection);
                logger.log(Level.DEBUG, "ProxyConnection was  created and added in connection pool");
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Error initializing connection pool", e);
            }
        }
    }

    public static ConnectionPool getInstance() {
        return createConnectionPool();
    }

    //for testing connections to a test database
    public static ConnectionPool getInstanceTest() {
        isTest = true;
        return createConnectionPool();
    }

    private static ConnectionPool createConnectionPool() {
        if (!instanceCreated.get()) {
            poolLock.lock();
            try {
                if (null == instance) {
                    instance = new ConnectionPool();
                    instanceCreated.set(true);
                    Timer timer = new Timer(true);
                    timer.scheduleAtFixedRate(new CheckingPoolConnection(), 3600000, 3600000);
                }
            } finally {
                poolLock.unlock();
            }
        }
        return instance;
    }

    public ProxyConnection getConnection() {
        ProxyConnection proxyConnection = null;
        try {
            accessLock.readLock().lock();
            proxyConnection = freeConnections.take();
            givenAwayConnections.offer(proxyConnection);
        } catch (InterruptedException e) {
            logger.log(Level.DEBUG, "Getting connection from pool was interrupted");
        } finally {
            accessLock.readLock().unlock();
        }
        return proxyConnection;
    }

    public void returnConnection(ProxyConnection proxyConnection) {
        try {
            accessLock.readLock().lock();
            givenAwayConnections.remove(proxyConnection);
            freeConnections.offer(proxyConnection);
        } finally {
            accessLock.readLock().unlock();
        }
    }

    private static void deregisterDrivers() {
        Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Exception in deregistration of drivers", e);
            }
        }
        logger.log(Level.DEBUG, "Finish deregistration of drivers");
    }

    public void destroyPool() {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            try {
                freeConnections.take().realClose();
            } catch (SQLException | InterruptedException e) {
                logger.log(Level.ERROR, "Exception in destroy pool", e);
            }
        }
        deregisterDrivers();
        logger.log(Level.DEBUG, "DestroyPool completed");
    }

    public Object clone() throws CloneNotSupportedException {
        if (null != instance) {
            throw new CloneNotSupportedException();
        }
        return this.clone();
    }

    void addConnectionIfNeeded() {
        try {
            accessLock.writeLock().lock();
            if (freeConnections.size() + givenAwayConnections.size() != DEFAULT_POOL_SIZE) {
                try {
                    freeConnections.offer(new ProxyConnection(
                            DriverManager.getConnection(properties.getProperty(ApplicationConstants.URL_KEY), properties.getProperty(ApplicationConstants.USER_KEY),
                                    properties.getProperty(ApplicationConstants.PASSWORD_KEY))));
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "Error to create connection", e);
                }
            }
        } finally {
            accessLock.writeLock().unlock();
        }
    }

    public Properties getProperties() {
        return properties;
    }
}
