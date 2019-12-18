package by.training.kolos.command;

import by.training.kolos.command.impl.EmptyCommand;

/**
 * Класс для получения команды
 *
 * @author Колос Марина
 */
public final class ActionFactory {

    private ActionFactory() {
    }

    /**
     * Метод по определению команды
     *
     * @param commandName передан из request в запросе для идентификации команды
     * @return объект команды из enum {@link CommandType}
     * В случае некорректного параметра или отсутсвия команды в списке возвращается команда {@link EmptyCommand}
     */
    public static AbstractCommand defineCommand(String commandName) {
        AbstractCommand current;
        if (commandName == null || commandName.isEmpty()) {
            return new EmptyCommand();
        }
        try {
            CommandType currentEnum = CommandType.valueOf(commandName.toUpperCase());
            current = currentEnum.getCurrentCommand();
        } catch (IllegalArgumentException e) {
            current = new EmptyCommand();
        }
        return current;
    }
}
