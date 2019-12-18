package by.training.kolos.service;

import by.training.kolos.entity.User;
import by.training.kolos.exception.ServiceException;

import java.util.List;

/**
 * Интерфейс для обработки запросов, связанных с пользователем
 *
 * @author Колос Марина
 */
public interface UserService {

    List<User> findAll() throws ServiceException;

    User find(String email, String password) throws ServiceException;

    User save(User user) throws ServiceException;
}
