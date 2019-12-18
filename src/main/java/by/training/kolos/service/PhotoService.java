package by.training.kolos.service;

import by.training.kolos.entity.Photo;
import by.training.kolos.exception.ServiceException;
/**
 * Интерфейс для обработки запросов, связанных с фото
 *
 * @author Колос Марина
 */
public interface PhotoService {

    Photo find(Long photoId) throws ServiceException;
}
