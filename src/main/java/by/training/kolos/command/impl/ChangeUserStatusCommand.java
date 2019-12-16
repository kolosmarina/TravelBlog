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

import static by.training.kolos.command.ApplicationConstants.*;

public class ChangeUserStatusCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(SessionRequestContent content) {
        String page;
        User currentUser = (User) content.getSessionAttribute(PARAM_USER);
        Long userIdForChangeStatus = Long.valueOf(content.getRequestParameter(PARAM_USER_ID)[0]);

        if (AccessAdministrationSecurity.checkUserRole(content, UserRole.ADMIN)) {
            Boolean isActive = Boolean.valueOf(content.getRequestParameter(PARAM_IS_ACTIVE_USER)[0]);
            try {
                ServiceFactory.getUserService().changeStatus(userIdForChangeStatus, isActive);
                logger.log(Level.DEBUG, "After change UserId-{}, UserStatus-{}", userIdForChangeStatus, isActive);
                page = content.getPreviousPage();
                content.setDirection(SessionRequestContent.Direction.REDIRECT);
            } catch (ServiceException e) {
                page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET), PARAM_COMMAND,
                        COMMAND_OPEN_ERROR_PAGE);
            }
        } else {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET), PARAM_COMMAND,
                    COMMAND_OPEN_MAIN_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
            logger.log(Level.DEBUG, "Access is denied, user has role - {}", currentUser.getUserRole());
        }
        return page;
    }
}
