package by.training.kolos.command;

import by.training.kolos.command.impl.EmptyCommand;

public final class ActionFactory {

    private ActionFactory() {
    }

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
