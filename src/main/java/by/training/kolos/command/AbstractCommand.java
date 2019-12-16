package by.training.kolos.command;

import by.training.kolos.controller.SessionRequestContent;

public interface AbstractCommand {
    String execute(SessionRequestContent content);
}
