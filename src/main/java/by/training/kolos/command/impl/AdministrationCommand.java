package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.command.AccessAdministrationSecurity;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.entity.User;
import by.training.kolos.entity.UserRole;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static by.training.kolos.command.ApplicationConstants.*;

public class AdministrationCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(SessionRequestContent content) {
        String page;
        User user = (User) content.getSessionAttribute(PARAM_USER);

        if (AccessAdministrationSecurity.checkUserRole(content, UserRole.ADMIN)) {
            page = ConfigurationManager.getProperty(PAGE_ADMINISTRATION);
            content.setDirection(SessionRequestContent.Direction.FORWARD);
            try {
                List<User> users = ServiceFactory.getUserService().findAll();
                List<User> notAdminUsers = users.stream()
                        .filter(it -> it.getUserRole() != UserRole.ADMIN).collect(Collectors.toList());
                content.setRequestAttribute(PARAM_USERS, notAdminUsers);
            } catch (ServiceException e) {
                page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET), PARAM_COMMAND,
                        COMMAND_OPEN_ERROR_PAGE);
                content.setDirection(SessionRequestContent.Direction.REDIRECT);
            }
        } else {
            logger.log(Level.DEBUG, "Access is denied, , user has role - {}", user.getUserRole());
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET), PARAM_COMMAND,
                    COMMAND_OPEN_MAIN_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        }
        return page;
    }
}
