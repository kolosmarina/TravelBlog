package by.training.kolos.service.impl;

import by.training.kolos.dao.impl.UserDaoImpl;
import by.training.kolos.entity.User;
import by.training.kolos.exception.DaoException;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private static final UserServiceImpl INSTANCE = new UserServiceImpl();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<User> findAll() throws ServiceException {
        try {
            return UserDaoImpl.getInstance().findAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User find(String email, String password) throws ServiceException {
        try {
            return UserDaoImpl.getInstance().findUseByEmailAndPassword(email, password);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User save(User user) throws ServiceException {
        try {
            return UserDaoImpl.getInstance().save(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean findUserStatus(Long userId) throws ServiceException {
        try {
            return UserDaoImpl.getInstance().findUserStatus(userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void changeStatus(Long userId, Boolean isActive) throws ServiceException {
        try {
            UserDaoImpl.getInstance().changeStatus(userId, isActive);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<User> findPopularUsersByWorldPart(int limit, String worldPart) throws ServiceException {
        try {
            return UserDaoImpl.getInstance().findPopularUsersByWorldPart(limit, worldPart);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public long countUsersByEmail(String email) throws ServiceException {
        try {
            return UserDaoImpl.getInstance().countUsersByEmail(email);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public long countUsersByNickname(String nickname) throws ServiceException {
        try {
            return UserDaoImpl.getInstance().countUsersByNickname(nickname);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
