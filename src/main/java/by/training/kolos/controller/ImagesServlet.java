package by.training.kolos.controller;

import by.training.kolos.command.ApplicationConstants;
import by.training.kolos.entity.Photo;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/images")
public class ImagesServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long photoId = Long.valueOf(req.getParameter(ApplicationConstants.PARAM_PHOTO_ID));
        try {
            Photo photo = ServiceFactory.getPhotoService().find(photoId);
            InputStream is = new FileInputStream(photo.getUrl());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            byte[] imageBytes = buffer.toByteArray();
            resp.setContentType("image/jpeg");
            resp.setContentLength(imageBytes.length);

            resp.getOutputStream().write(imageBytes);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "An error occurred in the ImagesServlet", e);
        }
    }
}
