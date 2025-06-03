package main.java.managers;

import connection.requests.AuthenticationRequest;
import connection.requests.Request;
import connection.responses.AuthenticationResponse;
import exceptions.AuthenticationException;
import main.java.connection.TCPClient;
import main.java.exceptions.ServerIsNotAvailableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.AuthenticationCommands;
import utility.Encoder;

import java.io.IOException;


public class AuthenticationManager {
    private static final Logger logger = LogManager.getLogger(AuthenticationManager.class);
    private static Runner runner;
    private static TCPClient client;
    private String login;
    private String password;

    public AuthenticationManager(Runner runner) {
        AuthenticationManager.runner = runner;
        AuthenticationManager.client = runner.getClient();
    }

    public boolean doAuthentication() throws ServerIsNotAvailableException {
        try{
            ConsoleManager.println("Введите login для входа в систему или register для регистрации.");
            String command = ConsoleManager.readObject();
            if (command == null) {
                return false;
            }
            if (command.equalsIgnoreCase("register")) {
                return register();
            } else if (command.equalsIgnoreCase("login")) {
                return login();
            } else {
                ConsoleManager.println("Неизвестная команда.");
                logger.warn("Введена неизвестная команда: {}", command);
                return false;
            }
        } catch (AuthenticationException e) {
            logger.warn("Произошла ошибка при аутентификации пользователя: {}", e.getMessage());
            ConsoleManager.printError(e.getMessage());
            ConsoleManager.println("Вы не вошли в систему.");
            return false;
        }
    }

    private boolean register() throws AuthenticationException, ServerIsNotAvailableException {
        try {
            ConsoleManager.println("Введите логин:");
            String login = ConsoleManager.readObject();
            logger.info("При регистрации введён логин {}.", login);

            ConsoleManager.println("Введите пароль:");
            String password = ConsoleManager.readObject();
            logger.info("Пароль введён при регистрации.");

            String hashedPassword = Encoder.hashPassword(password);
            logger.info("Пароль зашифрован при регистрации.");

            AuthenticationRequest authenticationRequest = formRequest(AuthenticationCommands.REGISTER, login, hashedPassword);
            byte[] data = client.serializeData(authenticationRequest);
            runner.sendData(data);
            AuthenticationResponse authenticationResponse = runner.readData();

            if (isAuthenticated(authenticationResponse)) {
                ConsoleManager.println("Регистрация прошла успешно!");
                ConsoleManager.println("Вы зашли в систему.");
                logger.info("Успешная регистрация нового пользователя. Логин: {}", login);
                this.login = login;
                this.password = hashedPassword;
                return true;
            }
            return false;
        } catch (IOException | ClassNotFoundException | AuthenticationException e) {
            logger.error("Произошла ошибка при регистрации нового пользователя.", e);
            throw new AuthenticationException("Ошибка при регистрации нового пользователя.");
        }
    }

    private boolean login() throws AuthenticationException, ServerIsNotAvailableException {
        try {
            ConsoleManager.println("Введите логин:");
            String login = ConsoleManager.readObject();
            logger.info("Логин введён при входе в систему. Логин: {}", login);

            ConsoleManager.println("Введите пароль:");
            String password = ConsoleManager.readObject();
            logger.info("Пароль введён при входе в систему.");

            String hashedPassword = Encoder.hashPassword(password);
            logger.info("Пароль зашифрован при входе в систему.");

            AuthenticationRequest authenticationRequest = formRequest(AuthenticationCommands.LOGIN, login, hashedPassword);
            byte[] data = client.serializeData(authenticationRequest);
            runner.sendData(data);
            AuthenticationResponse authenticationResponse = runner.readData();

            if (isAuthenticated(authenticationResponse)) {
                ConsoleManager.println("Успешный вход!");
                logger.info("Успешный вход в систему. Логин: {}", login);
                this.login = login;
                this.password = hashedPassword;
                return true;
            }
            return false;
        } catch (IOException | ClassNotFoundException | AuthenticationException e) {
            logger.error("Произошла ошибка при входе пользователя в систему: {}", e.getMessage());
            throw new AuthenticationException(e.getMessage());
        }
    }

    private AuthenticationRequest formRequest(AuthenticationCommands command, String login, String password) {
        return new AuthenticationRequest(command, login, password);
    }

    private boolean isAuthenticated(AuthenticationResponse response) throws AuthenticationException {
        if (response == null) {
            return false;
        }
        if (response.isAuthenticated()) {
            return true;
        }
        throw new AuthenticationException(response.getError());
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
