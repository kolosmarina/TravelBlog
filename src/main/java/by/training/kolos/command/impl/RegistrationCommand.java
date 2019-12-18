package by.training.kolos.command.impl;

import by.training.kolos.command.AbstractCommand;
import by.training.kolos.command.InputDataValidation;
import by.training.kolos.command.PasswordSecurity;
import by.training.kolos.config.ConfigurationManager;
import by.training.kolos.config.GoogleMailThread;
import by.training.kolos.config.MessageManager;
import by.training.kolos.controller.SessionRequestContent;
import by.training.kolos.entity.User;
import by.training.kolos.entity.UserRole;
import by.training.kolos.exception.ServiceException;
import by.training.kolos.service.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.training.kolos.command.ApplicationConstants.*;

/**
 * Класс для выполнения запроса по регистрации нового пользователя
 * с отправкой письма об успешной регистрации на email нового пользователя
 *
 * @author Колос Марина
 */
public class RegistrationCommand implements AbstractCommand {
    private static final Logger logger = LogManager.getLogger();

    private static final String SMTP_LOGIN;
    private static final String SMTP_PASSWORD;

    static {
        Path path = Paths.get(DATA_STORAGE_FOR_LETTERS);
        List<String> collect = new ArrayList<>();
        collect.add("fake");
        collect.add("1234");
        try {
            Stream<String> lineStream = Files.newBufferedReader(path).lines();
            collect = lineStream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SMTP_LOGIN = collect.get(0).trim();
        SMTP_PASSWORD = collect.get(1).trim();
    }

    /**
     * @see AbstractCommand#execute(SessionRequestContent)
     */
    @Override
    public String execute(SessionRequestContent content) {
        String page;
        if (!InputDataValidation.inputValidationForRegistration(content)) {
            page = ConfigurationManager.getProperty(PAGE_REGISTRATION);
            content.setDirection(SessionRequestContent.Direction.FORWARD);
            return page;
        }

        try {
            if (!isUniqueParamValidation(content)) {
                page = ConfigurationManager.getProperty(PAGE_REGISTRATION);
                content.setDirection(SessionRequestContent.Direction.FORWARD);
                return page;
            }

            String hashedPassword = PasswordSecurity.getHashedPassword(content.getRequestParameter(PARAM_PASSWORD)[0].trim());
            User user = User.builder()
                    .role(UserRole.USER)
                    .password(hashedPassword)
                    .nickname(content.getRequestParameter(PARAM_NICKNAME)[0].trim())
                    .email(content.getRequestParameter(PARAM_EMAIL)[0].trim().toLowerCase())
                    .isActive(true)
                    .registrationDate(Instant.now().toEpochMilli())
                    .build();

            user = ServiceFactory.getUserService().save(user);
            if (user.getId() != null) {
                //sending an e-mail about successful registration
                GoogleMailThread googleMailThread = new GoogleMailThread(SMTP_LOGIN, SMTP_PASSWORD,
                        MessageManager.getProperty(MESSAGE_TITLE_LETTER), user.getEmail(),
                        MessageManager.getProperty(MESSAGE_BODY_LETTER));
                googleMailThread.start();
                page = String.format("%s?%s=%s&%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                        PARAM_COMMAND, COMMAND_OPEN_REGISTRATION_PAGE,
                        PARAM_SUCCESS_REGISTRATION,
                        MessageManager.getProperty(MESSAGE_SUCCESS_REGISTRATION));
                content.setDirection(SessionRequestContent.Direction.REDIRECT);
                logger.log(Level.DEBUG, "User-{} has been saved", user.getId());
            } else {
                content.setRequestAttribute(PARAM_ERROR_REGISTRATION,
                        MessageManager.getProperty(MESSAGE_REGISTRATION_ERROR));
                page = ConfigurationManager.getProperty(PAGE_REGISTRATION);
                content.setDirection(SessionRequestContent.Direction.FORWARD);
                logger.log(Level.DEBUG, "User was not saved");
            }
        } catch (ServiceException e) {
            page = String.format("%s?%s=%s", ConfigurationManager.getProperty(MAIN_SERVLET),
                    PARAM_COMMAND, COMMAND_OPEN_ERROR_PAGE);
            content.setDirection(SessionRequestContent.Direction.REDIRECT);
        }
        return page;
    }

    private boolean isUniqueParamValidation(SessionRequestContent content) throws ServiceException {
        boolean result = true;
        long numberUsersByEmail = ServiceFactory.getUserService()
                .countUsersByEmail(content.getRequestParameter(PARAM_EMAIL)[0].trim().toLowerCase());
        long numberUsersByNickname = ServiceFactory.getUserService()
                .countUsersByNickname(content.getRequestParameter(PARAM_NICKNAME)[0].trim());
        if (numberUsersByEmail != 0) {
            content.setRequestAttribute(PARAM_INVALID_EMAIL,
                    MessageManager.getProperty(MESSAGE_REGISTRATION_NOT_UNIQUE_EMAIL_ERROR));
            result = false;
        }
        if (numberUsersByNickname != 0) {
            content.setRequestAttribute(PARAM_INVALID_NICKNAME,
                    MessageManager.getProperty(MESSAGE_REGISTRATION_NOT_UNIQUE_NICKNAME_ERROR));
            result = false;
        }
        return result;
    }
}
