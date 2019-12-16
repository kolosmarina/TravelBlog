package by.training.kolos.dao;

import by.training.kolos.entity.User;
import by.training.kolos.exception.DaoException;

public interface UserDao extends BaseDao<Long, User> {

    User findUseByEmailAndPassword(String email, String password) throws DaoException;
}
