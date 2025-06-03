package main.java.handlers;

import commands.Command;
import commands.Report;
import connection.requests.AuthenticationRequest;
import connection.requests.CommandRequest;
import connection.requests.Request;
import connection.requests.UserRequest;
import connection.responses.*;
import main.java.connection.AbstractTCPServer;
import main.java.managers.AuthenticationManager;
import main.java.managers.CommandManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;

public class ClientHandler implements Runnable {
    private final AbstractTCPServer server;
    private final AuthenticationManager authenticationManager;
    private final Socket socket;
    private boolean connection = false;
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);

    public ClientHandler(AbstractTCPServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.authenticationManager = new AuthenticationManager(this);
    }

    public void sendCommands() {
        try {
            HashMap<String, Command> commands = CommandManager.getCommands();
            CommandMapResponse commandsMapResponse = server.formListOfCommandsResponse(commands);
            byte[] dataCommands = server.serializeResponse(commandsMapResponse);
            server.sendData(socket, dataCommands);
            logger.info("Информация о доступных командах отправлена клиенту.");
        } catch (IOException e) {
            logger.error("Ошибка при отправке клиенту данных о доступных командах.", e);
            stop();
        }
    }

    public void sendKeyList() {
        try {
            KeyListResponse keyListResponse = server.formKeyListResponse();
            byte[] dataKeys = server.serializeResponse(keyListResponse);
            server.sendData(socket, dataKeys);
            logger.info("Информация о ключах элементов коллекции отправлена клиенту.");
        } catch (IOException e) {
            logger.error("Ошибка при отправке клиенту информации о ключах элементов коллекции.", e);
            stop();
        }
    }

    public void sendIdList() {
        try {
            IdListResponse idListResponse = server.formIdListResponse();
            byte[] dataId = server.serializeResponse(idListResponse);
            server.sendData(socket, dataId);
            logger.info("Информация о id элементов коллекции отправлена клиенту.");
        } catch (IOException e) {
            logger.error("Ошибка при отправке клиенту информации о id элементов коллекции.", e);
            stop();
        }
    }

    public void closeSocket() {
        try {
            socket.close();
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
        sendCommands();
        sendKeyList();
        sendIdList();
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
            report = server.handleRequest(request);
            logger.info("Данные клиента обработаны.");
        } catch (Exception e) {
            logger.error("Ошибка при обработке данных клиента", e);
            stop();
        }
        return report;
    }

    public CommandResponse getCommandResponse(Report report) {
        CommandResponse commandResponse = null;
        try {
            commandResponse = server.formCommandResponse(report);
            logger.info("Сформирован ответ клиенту");
        } catch (Exception e) {
            logger.error("Ошибка при формировании ответа клиенту", e);
            stop();
        }
        return commandResponse;
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
        try {
            server.sendData(socket, data);
            logger.info("Данные отправлены клиенту");
        } catch (IOException e) {
            logger.error("Ошибка при отправке данных клиенту", e);
            stop();
        }
    }

    public void run() {
        try {
            connection = true;
            start();
            while (connection) {
                CommandRequest request = receiveUserRequest();
                Report report = getReport(request);
                CommandResponse commandResponse = getCommandResponse(report);
                byte[] responseData = serializeResponse(commandResponse);
                sendData(responseData);
            }
        }
        catch (Exception e) {
            logger.error("Непредвиденная ошибка.", e);
        }
        finally {
            stop();
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
