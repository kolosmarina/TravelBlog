package by.training.kolos.controller;

import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.entity.User;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.training.kolos.command.ApplicationConstants.*;

@WebFilter(urlPatterns = {"/travelling"})
public class AuthorizationFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String parameter = request.getParameter(PARAM_COMMAND);
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute(PARAM_USER);
        if (COMMAND_ADMINISTRATION.equals(parameter)
                || COMMAND_CHANGE_USER_STATUS.equals(parameter)
                || COMMAND_OPEN_NEW_POST_PAGE.equals(parameter)
                || COMMAND_SET_MAIN_PHOTO.equals(parameter)
                || COMMAND_DELETE_POST.equals(parameter)
                || COMMAND_OPEN_SUCCESS_DELETE_POST_PAGE.equals(parameter)
                || COMMAND_OPEN_NEW_COMMENT_PAGE.equals(parameter)
                || COMMAND_SAVE_COMMENT.equals(parameter)
                || COMMAND_DELETE_COMMENT.equals(parameter)
                || COMMAND_SAVE_LIKE.equals(parameter)
                || COMMAND_DELETE_LIKE.equals(parameter)
                || COMMAND_LOGOUT.equals(parameter)) {
            if (user == null) {
                logger.log(Level.DEBUG, "User is not registered, access to the page with command-{} is denied", parameter);
                String page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                        PARAM_COMMAND, COMMAND_OPEN_MAIN_PAGE);
                ((HttpServletResponse) response).sendRedirect(req.getContextPath() + page);
            } else {
                boolean isActive = false;
                try {
                    isActive = ServiceFactory.getUserService().findUserStatus(user.getId());
                } catch (ServiceException e) {
                    String page = ConfigurationManager.getProperty(PAGE_ERROR);
                    ((HttpServletResponse) response).sendRedirect(req.getContextPath() + page);
                }
                if (!isActive) {
                    session.invalidate();
                    logger.log(Level.DEBUG, "User is blocked, access to the page with command-{} is denied", parameter);
                    String page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                            PARAM_COMMAND, COMMAND_OPEN_MAIN_PAGE);
                    ((HttpServletResponse) response).sendRedirect(req.getContextPath() + page);
                } else {
                    chain.doFilter(request, response);
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
