package main.java.handlers;

import commands.Report;
import connection.requests.AuthenticationRequest;
import connection.requests.CommandRequest;
import connection.requests.UserRequest;
import connection.responses.*;
import main.java.connection.AbstractTCPServer;
import main.java.managers.AuthenticationManager;
import main.java.managers.CommandManager;
import main.java.managers.ThreadPoolManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ClientHandler implements Runnable {
    private final AbstractTCPServer server;
    private final AuthenticationManager authenticationManager;
    private final Socket socket;
    private volatile boolean connection = false;
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    private final ThreadPoolManager threadPoolManager;

    public ClientHandler(AbstractTCPServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.authenticationManager = new AuthenticationManager(this);
        this.threadPoolManager = ThreadPoolManager.getInstance();
    }

    public void sendStartInfo() {
        try {
            CommandMapResponse commandsMapResponse = server.formListOfCommandsResponse(CommandManager.getCommands());
            KeyListResponse keyListResponse = server.formKeyListResponse();
            IdListResponse idListResponse = server.formIdListResponse();

            StartInfoResponse startInfoResponse = new StartInfoResponse(commandsMapResponse, keyListResponse, idListResponse);
            byte[] data = server.serializeResponse(startInfoResponse);
            server.sendData(socket, data);
            logger.info("Информация, необходимая для начала работы, отправлена клиенту.");
        } catch (IOException e) {
            logger.error("Ошибка при отправке клиенту данных.", e);
            stop();
        }
    }

    public void sendCompositeResponse(Report report) {
        CommandResponse commandResponse = server.formCommandResponse(report);
        KeyListResponse keyListResponse = server.formKeyListResponse();
        IdListResponse idListResponse = server.formIdListResponse();

        CompositeResponse compositeResponse = new CompositeResponse(commandResponse, keyListResponse, idListResponse);
        byte[] data = server.serializeResponse(compositeResponse);
        sendData(data);
        logger.info("Информация о результате выполнения команды, о ключах элементов " +
                "и об id элементов коллекции отправлена клиенту.");
    }

    public void closeSocket() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                logger.info("Сокет закрыт успешно");
            }
        } catch (IOException e) {
            logger.error("Ошибка при закрытии сокета.", e);
        }
    }

    public void stop() {
        logger.info("Отключение от клиента.");
        connection = false;
        closeSocket();
    }

    public void start() {
        boolean authenticated = false;
        while (! authenticated) {
            authenticated = authenticationManager.doAuthentication();
        }
        sendStartInfo();
        logger.info("Произведены действия для начала работы с клиентом.");
    }

    public AuthenticationRequest receiveAuthenticationRequest() {
        AuthenticationRequest request = null;
        try {
            request = server.receiveObject(socket);
            logger.info("Запрос на аутентификацию получен от клиента {}", request.getUsername());
        } catch (Exception e) {
            logger.error("Ошибка при получении данных клиента", e);
            stop();
        }
        return request;
    }

    public <T extends UserRequest> T receiveUserRequest() {
        T request = null;
        try {
            request = server.receiveObject(socket);
            logger.info("Запрос получен от клиента {}", request.getUsername());
            if (! catchUserRequest(request)) {
                logger.error("Ошибка при сверке логина и пароля клиента.");
                stop();
            }
            logger.info("В запросе указаны верные логин и пароль.");
        } catch (Exception e) {
            logger.error("Ошибка при получении данных клиента", e);
            stop();
        }
        return request;
    }

    public boolean catchUserRequest(UserRequest userRequest) {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        if (!Objects.equals(username, authenticationManager.getUsername())) {
            return false;
        }
        if (!Objects.equals(password, authenticationManager.getPassword())) {
            return false;
        }
        return true;
    }

    public Report getReport(CommandRequest request) {
        Report report = null;
        try {
            // Создаем асинхронную задачу
            CompletableFuture<Report> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return server.handleRequest(request);
                } catch (Exception e) {
                    logger.error("Ошибка при обработке данных клиента", e);
                    return null;
                }
            }, threadPoolManager.getRequestProcessorPool()); // Запуск задачи в пуле потоков

            // Получаем результат
            report = future.get();
            logger.info("Данные клиента обработаны.");
        } catch (Exception e) {
            logger.error("Ошибка при обработке данных клиента", e);
            stop();
        }
        return report;
    }

    public byte[] serializeResponse(Response response) {
        byte[] responseData = null;
        try {
            responseData = server.serializeResponse(response);
            logger.info("Данные сериализованы");
        } catch (Exception e) {
            logger.error("Ошибка сериализации ответа клиенту", e);
            stop();
        }
        return responseData;
    }

    public void sendData(byte[] data) {
        threadPoolManager.getResponseSenderPool().submit(() -> {
            try {
                server.sendData(socket, data);
                logger.info("Данные отправлены клиенту");
            } catch (IOException e) {
                logger.error("Ошибка при отправке данных клиенту", e);
                stop();
            }
        });
    }

    public void run() {
        try {
            connection = true;
            start();
            while (connection) {
                CompletableFuture<CommandRequest> requestFuture = CompletableFuture.supplyAsync(() -> {
                    try {
                        return receiveUserRequest();
                    } catch (Exception e) {
                        logger.error("Ошибка при получении запроса от клиента.", e);
                        return null;
                    }
                }, threadPoolManager.getRequestReaderPool());

                CommandRequest request = requestFuture.get();
                if (request != null) {
                    Report report = getReport(request);
                    sendCompositeResponse(report);
                }
            }
        } catch (Exception e) {
            logger.error("Непредвиденная ошибка.", e);
        } finally {
            stop();
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
