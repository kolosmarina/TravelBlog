package by.training.kolos.service;

import by.training.kolos.entity.Tag;
import by.training.kolos.exception.ServiceException;

import java.util.List;

public interface TagService {

    List<Tag> findTagsByPhoto(Long photoId) throws ServiceException;
}
