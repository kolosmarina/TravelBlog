package by.training.kolos.service;

import by.training.kolos.entity.Photo;
import by.training.kolos.exception.ServiceException;

public interface PhotoService {

    Photo find(Long photoId) throws ServiceException;
}
