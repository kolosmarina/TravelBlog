package by.training.kolos.controller;

import by.training.kolos.command.ActionFactory;
import by.training.kolos.command.AbstractCommand;
import by.training.kolos.command.ApplicationConstants;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.connection.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/travelling")
public class ControllerServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName = req.getParameter(ApplicationConstants.PARAM_COMMAND);
        logger.log(Level.DEBUG, "ControllerServlet: command-{}", commandName);
        SessionRequestContent content = new SessionRequestContent(req);
        AbstractCommand command = ActionFactory.defineCommand(commandName);
        String page = command.execute(content);
        content.updateRequest(req);
        if (page != null) {
            if (content.getDirection() == SessionRequestContent.Direction.FORWARD) {
                getServletContext()
                        .getRequestDispatcher(page)
                        .forward(req, resp);
            }
            if (content.getDirection() == SessionRequestContent.Direction.REDIRECT) {
                resp.sendRedirect(req.getContextPath() + page);
            }
        } else {
            logger.log(Level.DEBUG, "Received parameter page=null");
            page = ConfigurationManager.getProperty(ApplicationConstants.PAGE_INDEX);
            resp.sendRedirect(req.getContextPath() + page);
        }
    }

    @Override
    public void init() {
        ConnectionPool.getInstance();
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().destroyPool();
    }
}
