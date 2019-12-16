package by.training.kolos.command.impl;

import by.training.kolos.command.ApplicationConstants;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.dao.impl.LikeDaoImpl;
import by.training.kolos.entity.Like;
import by.training.kolos.entity.User;
import by.training.kolos.exception.DaoException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class SaveLikeCommandTest extends BaseCommandTest {

    @Test
    public void likeSaveTest() throws DaoException {
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(ApplicationConstants.PARAM_USER, User.builder().id(USER_ID).build());
        Map<String, String[]> reqParameters = new HashMap<>();
        reqParameters.put(ApplicationConstants.PARAM_PHOTO_ID, new String[]{PHOTO_ID.toString()});
        Map<String, Object> reqAttributes = new HashMap<>();
        String expected = "http//previousPage";
        SessionRequestContent content = new SessionRequestContent(sessionAttributes, reqAttributes, reqParameters,
                null, expected, false);

        DeleteLikeCommand command = new DeleteLikeCommand();
        String actual = command.execute(content);

        Assert.assertEquals(actual, expected);
    }

    @AfterClass
    public void cleanUp() throws DaoException {
        Like like = Like.builder().photoId(PHOTO_ID).userId(USER_ID).build();
        LikeDaoImpl.getInstance().delete(like);
    }

    @Test
    public void likeSaveWithExceptionTest() throws DaoException {
        Long userId = 1L;
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(ApplicationConstants.PARAM_USER, User.builder().id(userId).build());
        Map<String, String[]> reqParameters = new HashMap<>();
        reqParameters.put(ApplicationConstants.PARAM_PHOTO_ID, new String[]{"-1"});
        Map<String, Object> reqAttributes = new HashMap<>();
        String expected = String.format("%s?%s=%s", ConfigurationManager.getProperty(ApplicationConstants.MAIN_SERVLET),
                ApplicationConstants.PARAM_COMMAND, ApplicationConstants.COMMAND_OPEN_ERROR_PAGE);
        SessionRequestContent content = new SessionRequestContent(sessionAttributes, reqAttributes, reqParameters,
                null, expected, false);

        DeleteLikeCommand command = new DeleteLikeCommand();
        String actual = command.execute(content);

        Assert.assertEquals(actual, expected);
    }
}
