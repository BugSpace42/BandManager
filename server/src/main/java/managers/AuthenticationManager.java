package main.java.managers;

import connection.requests.AuthenticationRequest;
import connection.responses.AuthenticationResponse;
import exceptions.AuthenticationException;
import main.java.handlers.ClientHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.AuthenticationCommands;
import utility.Encoder;

import java.io.*;
import java.util.HashMap;

public class AuthenticationManager {
    private static final Logger logger = LogManager.getLogger(AuthenticationManager.class);
    private static final String USERS_FILE = "res/users.csv";
    private static HashMap<String, String> users = new HashMap<>();
    private final ClientHandler clientHandler;
    private String username;
    private String password;

    public AuthenticationManager(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public boolean doAuthentication() {
        try {
            AuthenticationRequest authenticationRequest = clientHandler.receiveAuthenticationRequest();
            loadUsers();
            if (authenticationRequest == null) {
                return false;
            }
            if (authenticationRequest.getCommand() == AuthenticationCommands.LOGIN) {
                if (login(authenticationRequest.getUsername(), authenticationRequest.getPassword())){
                    sendAuthenticationResponse(true, null);
                    this.username = authenticationRequest.getUsername();
                    this.password = authenticationRequest.getPassword();
                    return true;
                }
                else {
                    throw new AuthenticationException("Неправильный логин или пароль.");
                }
            } else if (authenticationRequest.getCommand() == AuthenticationCommands.REGISTER) {
                if (register(authenticationRequest.getUsername(), authenticationRequest.getPassword())) {
                    sendAuthenticationResponse(true, null);
                    this.username = authenticationRequest.getUsername();
                    this.password = authenticationRequest.getPassword();
                    return true;
                }
                else {
                    throw new AuthenticationException("Возникла проблема с регистрацией нового пользователя: " +
                            "Пользователь с логином " + authenticationRequest.getUsername() + " уже существует.");
                }
            } else {
                throw new AuthenticationException("Неизвестная команда аутентификации.");
            }
        } catch (AuthenticationException e) {
            sendAuthenticationResponse(false, e.getMessage());
            return false;
        }
    }

    private boolean register(String username, String password) throws AuthenticationException {
        if (users.containsKey(username)) {
            logger.info("При регистрации введён существующий логин: {}", username);
            logger.info("Регистрация прервана.");
            throw new AuthenticationException("При регистрации введён существующий логин.");
        }
        addUser(username, password);
        logger.info("Новый пользователь успешно добавлен. Логин: {}", username);
        saveUser(username, password);
        logger.info("Информация о новом пользователе успешно сохранена.");
        return true;
    }

    private boolean login(String username, String password) throws AuthenticationException {
        if (!users.containsKey(username)) {
            logger.info("Пользователь с логином {} не найден.", username);
            throw new AuthenticationException("Пользователь с логином " + username + " не найден.");
        }
        logger.info("Пользователь с введённым логином найден. Логин: {}", username);
        if (password.equals(users.get(username))) {
            logger.info("Успешный вход в систему. Логин: {}", username);
            return true;
        } else {
            logger.info("Введён неверный пароль.");
            throw new AuthenticationException("Введён неверный пароль.");
        }
    }

    private void loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            logger.warn("Файл с пользователями не найден.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String login = parts[0];
                    String passwordHash = parts[1];
                    users.put(login, passwordHash);
                }
            }
            logger.info("Загружено пользователей: " + users.size());
        } catch (IOException e) {
            logger.error("Ошибка при чтении файла.", e);
        }
    }

    private void saveUser(String login, String passwordHash) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            bw.write(login + "," + passwordHash);
            bw.newLine();
        } catch (IOException e) {
            logger.error("Ошибка при сохранении пользователя.", e);
        }
    }

    private void addUser(String username, String password) throws AuthenticationException {
        users.put(username, password);
    }

    private void sendAuthenticationResponse(boolean authenticated, String error) {
        AuthenticationResponse authenticationResponse = formAuthenticationResponse(authenticated, error);
        clientHandler.sendData(clientHandler.serializeResponse(authenticationResponse));
    }

    private AuthenticationResponse formAuthenticationResponse(boolean authenticated, String error) {
        return new AuthenticationResponse(authenticated, error);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
