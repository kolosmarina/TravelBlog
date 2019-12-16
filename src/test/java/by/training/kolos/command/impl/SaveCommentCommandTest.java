package by.training.kolos.command.impl;

import by.training.kolos.command.ApplicationConstants;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.dao.impl.CommentDaoImpl;
import by.training.kolos.entity.User;
import by.training.kolos.exception.DaoException;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

public class SaveCommentCommandTest extends BaseCommandTest {
    private String value = "new comment";

    @Test
    public void commentSaveTest() {
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(ApplicationConstants.PARAM_USER, User.builder().id(USER_ID).build());
        Map<String, String[]> reqParameters = new HashMap<>();
        reqParameters.put(ApplicationConstants.PARAM_PHOTO_ID, new String[]{PHOTO_ID.toString()});
        reqParameters.put(ApplicationConstants.PARAM_VALUE_COMMENT, new String[]{value});
        Map<String, Object> reqAttributes = new HashMap<>();
        String expected = String.format("%s?%s=%d&%s=%s", ConfigurationManager.getProperty(ApplicationConstants.MAIN_SERVLET),
                ApplicationConstants.PARAM_PHOTO_ID, PHOTO_ID, ApplicationConstants.PARAM_COMMAND,
                ApplicationConstants.COMMAND_OPEN_PHOTO_PAGE);
        SessionRequestContent content = new SessionRequestContent(sessionAttributes, reqAttributes, reqParameters,
                null, expected, false);

        SaveCommentCommand command = new SaveCommentCommand();
        String actual = command.execute(content);

        Assert.assertEquals(actual, expected);
    }

    @AfterClass
    public void cleanUp() throws DaoException {
        CommentDaoImpl.getInstance().delete(PHOTO_ID, USER_ID);
    }


    @Test
    public void emptyCommentSaveTest() {
        value = "";
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(ApplicationConstants.PARAM_USER, User.builder().id(USER_ID).build());
        Map<String, String[]> reqParameters = new HashMap<>();
        reqParameters.put(ApplicationConstants.PARAM_PHOTO_ID, new String[]{PHOTO_ID.toString()});
        reqParameters.put(ApplicationConstants.PARAM_VALUE_COMMENT, new String[]{value});
        Map<String, Object> reqAttributes = new HashMap<>();
        String expected = String.format("%s?%s=%s&%s=%s", ConfigurationManager.getProperty(ApplicationConstants.MAIN_SERVLET),
                ApplicationConstants.PARAM_COMMAND, ApplicationConstants.COMMAND_OPEN_PHOTO_PAGE, ApplicationConstants.PARAM_PHOTO_ID, PHOTO_ID);
        SessionRequestContent content = new SessionRequestContent(sessionAttributes, reqAttributes, reqParameters,
                null, expected, false);

        SaveCommentCommand command = new SaveCommentCommand();
        String actual = command.execute(content);

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void commentSaveWithExceptionTest() {
        value = "new comment";
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(ApplicationConstants.PARAM_USER, User.builder().id(-1L).build());
        Map<String, String[]> reqParameters = new HashMap<>();
        reqParameters.put(ApplicationConstants.PARAM_PHOTO_ID, new String[]{PHOTO_ID.toString()});
        reqParameters.put(ApplicationConstants.PARAM_VALUE_COMMENT, new String[]{value});
        Map<String, Object> reqAttributes = new HashMap<>();
        String expected = String.format("%s?%s=%s", ConfigurationManager.getProperty(ApplicationConstants.MAIN_SERVLET),
                ApplicationConstants.PARAM_COMMAND, ApplicationConstants.COMMAND_OPEN_ERROR_PAGE);
        SessionRequestContent content = new SessionRequestContent(sessionAttributes,
                reqAttributes, reqParameters, null, expected, false);

        SaveCommentCommand command = new SaveCommentCommand();
        String actual = command.execute(content);

        Assert.assertEquals(actual, expected);
    }
}
