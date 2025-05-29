package main.java.managers;

import exceptions.AuthenticationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;


public class AuthenticationManager {
    private static final HashMap<String, String> users = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(AuthenticationManager.class);
    private static final String USERS_FILE = "res/users.csv"; // для сервера

    public AuthenticationManager() {}

    private static void loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            logger.info("Файл с пользователями не найден.");
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

    private static void saveUser(String login, String passwordHash) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            bw.write(login + "," + passwordHash);
            bw.newLine();
        } catch (IOException e) {
            logger.error("Ошибка при сохранении пользователя.", e);
        }
    }

    public static boolean doAuthentication() {
        try{
            loadUsers();
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
            logger.error("Произошла ошибка при аутентификации пользователя.", e);
            return false;
        }
    }

    private static boolean register() throws AuthenticationException {
        try {
            ConsoleManager.println("Введите логин:");
            String login = ConsoleManager.readObject();
            logger.info("При регистрации введён логин {}.", login);

            if (users.containsKey(login)) {
                logger.info("При регистрации введён существующий логин.");
                logger.info("Регистрация прервана.");
                ConsoleManager.println("Этот логин уже занят.");
                return false;
            }

            ConsoleManager.println("Введите пароль:");
            String password = ConsoleManager.readObject();
            logger.info("Пароль введён при регистрации.");

            String hashedPassword = hashPassword(password);
            logger.info("Пароль зашифрован при регистрации.");

            addUser(login, hashedPassword);
            logger.info("Новый пользователь успешно добавлен. Логин: {}", login);
            saveUser(login, hashedPassword);
            logger.info("Информация о новом пользователе успешно сохранена.");
            ConsoleManager.println("Регистрация прошла успешно!");
            return true;
        } catch (Exception e) {
            logger.error("Произошла ошибка при регистрации нового пользователя.", e);
            throw new AuthenticationException("Ошибка при регистрации нового пользователя.");
        }
    }

    private static boolean login() throws AuthenticationException {
        try {
            ConsoleManager.println("Введите логин:");
            String login = ConsoleManager.readObject();
            logger.info("Логин введён при входе в систему. Логин: {}", login);

            if (!users.containsKey(login)) {
                ConsoleManager.println("Пользователь не найден.");
                logger.info("Пользователь с введённым логином не найден.");
                return false;
            }
            logger.info("Пользователь с введённым логином найден. Логин: {}", login);

            ConsoleManager.println("Введите пароль:");
            String password = ConsoleManager.readObject();
            logger.info("Пароль введён при входе в систему.");

            String hashedPassword = hashPassword(password);
            logger.info("Пароль зашифрован при входе в систему.");
            if (hashedPassword.equals(users.get(login))) {
                ConsoleManager.println("Успешный вход!");
                logger.info("Успешный вход в систему. Логин: {}", login);
                return true;
            } else {
                ConsoleManager.println("Неверный пароль.");
                logger.info("Введён неверный пароль.");
                return false;
            }
        } catch (Exception e) {
            logger.error("Произошла ошибка при регистрации нового пользователя.", e);
            throw new AuthenticationException("Ошибка при регистрации нового пользователя.");
        }
    }

    private static String hashPassword(String password) throws AuthenticationException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            byte[] hashBytes = md.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("Ошибка при хэшировании пароля.", e);
            throw new AuthenticationException("Ошибка при хэшировании пароля.");
        }
    }

    private static void addUser(String username, String password) throws AuthenticationException {
        users.put(username, hashPassword(password));
    }
}
