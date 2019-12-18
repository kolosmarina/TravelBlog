package by.training.kolos.command;

import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.entity.User;
import by.training.kolos.entity.UserRole;

/**
 * Класс для валидации ролей пользователей
 *
 * @author Колос Марина
 */
public class AccessAdministrationSecurity {
    /**
     * Метод по валидации роли пользователя и переданной роли
     *
     * @param content содержит всю информацию из request от клиента
     * @param role    роль, по которой проводится валидация
     * @return true, если пользователь имеет роль идентичную с переданной
     */
    public static boolean checkUserRole(SessionRequestContent content, UserRole role) {
        User user = (User) content.getSessionAttribute(ApplicationConstants.PARAM_USER);
        return user.getUserRole() == role;
    }
}
