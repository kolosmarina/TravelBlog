package by.training.kolos.command;

import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.entity.User;
import by.training.kolos.entity.UserRole;

public class AccessAdministrationSecurity {

    public static boolean checkUserRole(SessionRequestContent content, UserRole role) {
        User user = (User) content.getSessionAttribute(ApplicationConstants.PARAM_USER);
        return user.getUserRole() == role;
    }
}
