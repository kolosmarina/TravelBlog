package by.training.kolos.service;

import by.training.kolos.entity.Like;
import by.training.kolos.exception.ServiceException;
/**
 * Интерфейс для обработки запросов, связанных с лайком
 *
 * @author Колос Марина
 */
public interface LikeService {

    Like save(Like like) throws ServiceException;

    boolean delete(Like like) throws ServiceException;

    Like find(Long photoId, Long userId) throws ServiceException;
}
