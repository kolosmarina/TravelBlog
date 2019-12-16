package by.training.kolos.command.impl;

import by.training.kolos.command.ApplicationConstants;
import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.dao.impl.CommentDaoImpl;
import by.training.kolos.entity.Comment;
import by.training.kolos.entity.User;
import by.training.kolos.exception.DaoException;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class DeleteCommentCommandTest extends BaseCommandTest {

    @Test
    public void commentDeletedTest() throws DaoException {
        String commentValue = "new comment";
        Long photoId = 1L;
        Long commentId = insertComment(commentValue, USER_ID, photoId);
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(ApplicationConstants.PARAM_USER, User.builder().id(USER_ID).build());
        Map<String, String[]> reqParameters = new HashMap<>();
        reqParameters.put(ApplicationConstants.PARAM_COMMENT_ID, new String[]{commentId.toString()});
        Map<String, Object> reqAttributes = new HashMap<>();
        String expected = "http//previousPage";
        SessionRequestContent content = new SessionRequestContent(sessionAttributes, reqAttributes, reqParameters, null, expected, false);

        DeleteCommentCommand command = new DeleteCommentCommand();
        String actual = command.execute(content);

        Assert.assertEquals(actual, expected);
    }

    private Long insertComment(String comment, Long userId, Long photoId) throws DaoException {
        return CommentDaoImpl.getInstance().save(Comment.builder().userId(userId).photoId(photoId).value(comment).publishDate(Instant.now().toEpochMilli()).build()).getId();
    }

    @Test
    public void commentNonexistentDeletedTest() {
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(ApplicationConstants.PARAM_USER, User.builder().id(USER_ID).build());
        Map<String, String[]> reqParameters = new HashMap<>();
        reqParameters.put(ApplicationConstants.PARAM_COMMENT_ID, new String[]{"-1"});
        Map<String, Object> reqAttributes = new HashMap<>();
        String expected = "http//previousPage";
        SessionRequestContent content = new SessionRequestContent(sessionAttributes, reqAttributes, reqParameters, null, expected, false);

        DeleteCommentCommand command = new DeleteCommentCommand();
        String actual = command.execute(content);

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void commentOtherUserDeletedTest() throws DaoException {
        Long otherUserId = -1L;
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(ApplicationConstants.PARAM_USER, User.builder().id(otherUserId).build());
        Map<String, String[]> reqParameters = new HashMap<>();
        reqParameters.put(ApplicationConstants.PARAM_COMMENT_ID, new String[]{"1"});
        Map<String, Object> reqAttributes = new HashMap<>();
        String expected = "http//previousPage";
        SessionRequestContent content = new SessionRequestContent(sessionAttributes, reqAttributes, reqParameters, null, expected, false);

        DeleteCommentCommand command = new DeleteCommentCommand();
        String actual = command.execute(content);

        Assert.assertEquals(actual, expected);
    }
}
