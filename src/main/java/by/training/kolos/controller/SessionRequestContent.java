package by.training.kolos.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для создания объектов, содержащих информацию из запроса
 *
 * @author Колос Марина
 */
public class SessionRequestContent {
    /**
     * Для получения из запроса адреса предыдущей страницы
     */
    private static final String SPECIAL_HEADER_NAME_FOR_PREVIOUS_PAGE = "Referer";

    public enum Direction {
        FORWARD,
        REDIRECT
    }

    private Map<String, Object> sessionAttributes;
    private Map<String, Object> reqAttributes;
    private Map<String, String[]> reqParameters;
    private Direction direction;
    private String previousPage;
    private boolean isInvalidated;

    public SessionRequestContent(HttpServletRequest req) {
        initContent(req);
    }

    //for testing with test database
    public SessionRequestContent(Map<String, Object> sessionAttributes, Map<String, Object> reqAttributes,
                                 Map<String, String[]> reqParameters, Direction direction, String previousPage,
                                 boolean isInvalidated) {
        this.sessionAttributes = sessionAttributes;
        this.reqAttributes = reqAttributes;
        this.reqParameters = reqParameters;
        this.direction = direction;
        this.previousPage = previousPage;
        this.isInvalidated = isInvalidated;
    }

    private void initContent(HttpServletRequest req) {
        reqParameters = req.getParameterMap();
        reqAttributes = new HashMap<>();
        Enumeration<String> reqAttributeNames = req.getAttributeNames();
        while (reqAttributeNames.hasMoreElements()) {
            String attributeName = reqAttributeNames.nextElement();
            reqAttributes.put(attributeName, req.getAttribute(attributeName));
        }
        HttpSession currentSession = req.getSession();
        Enumeration<String> sessionAttributeNames = currentSession.getAttributeNames();
        sessionAttributes = new HashMap<>();
        while (sessionAttributeNames.hasMoreElements()) {
            String attributeName = sessionAttributeNames.nextElement();
            sessionAttributes.put(attributeName, currentSession.getAttribute(attributeName));
        }
        if (req.getHeader(SPECIAL_HEADER_NAME_FOR_PREVIOUS_PAGE) == null) {
            previousPage = null;
        } else {
            previousPage = req.getHeader(SPECIAL_HEADER_NAME_FOR_PREVIOUS_PAGE).split(req.getContextPath())[1];
        }
    }

    public Object getSessionAttribute(String key) {
        return sessionAttributes.get(key);
    }

    public void setSessionAttribute(String key, Object value) {
        this.sessionAttributes.put(key, value);
    }

    public Object getRequestAttribute(String key) {
        return reqAttributes.get(key);
    }

    public void setRequestAttribute(String key, Object value) {
        this.reqAttributes.put(key, value);
    }

    public String[] getRequestParameter(String key) {
        return reqParameters.get(key);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public boolean isInvalidated() {
        return isInvalidated;
    }

    public void invalidateSession() {
        isInvalidated = true;
    }

    /**
     * Метод по обновлению запроса от клиента с учетом выполненных команд в системе
     */
    public void updateRequest(HttpServletRequest req) {
        reqAttributes.forEach(req::setAttribute);
        sessionAttributes.forEach(req.getSession()::setAttribute);
        if (isInvalidated) {
            req.getSession().invalidate();
        }
    }
}
