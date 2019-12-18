package by.training.kolos.command;

import by.training.kolos.controller.SessionRequestContent;

/**
 * Интерфейс по обработке запроса от клиента
 *
 * @author Колос Марина
 */
public interface AbstractCommand {
    /**
     * Метод по обработке запроса от клиента
     *
     * @param content содержит всю информацию из request от клиента
     * @return адрес страницы, на которую будет перенаправлен ответ клиенту
     * Во время выполнении команды в объект {@link SessionRequestContent} добавляются атрибуты с дополнительной информацией
     * для формирования ответа клиенту
     */
    String execute(SessionRequestContent content);
}
