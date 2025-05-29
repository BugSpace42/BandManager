package main.java.handlers;

import commands.Command;
import commands.Report;
import connection.requests.CommandRequest;
import connection.responses.CommandMapResponse;
import connection.responses.CommandResponse;
import connection.responses.IdListResponse;
import connection.responses.KeyListResponse;
import main.java.connection.AbstractTCPServer;
import main.java.managers.CommandManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler implements Runnable {
    private final AbstractTCPServer server;
    private final Socket socket;
    private boolean connection = false;
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);

    public ClientHandler(AbstractTCPServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
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
        closeSocket();
    }

    public void start() {
        sendCommands();
        sendKeyList();
        sendIdList();
        logger.info("Произведены действия для начала работы с клиентом.");
    }

    public CommandRequest receiveCommandRequest() {
        CommandRequest request = null;
        try {
            request = server.receiveObject(socket);
            logger.info("Запрос получен от клиента");
        } catch (Exception e) {
            logger.error("Ошибка при получении данных клиента", e);
            stop();
        }
        return request;
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

    public byte[] serializeCommandResponse(CommandResponse commandResponse) {
        byte[] responseData = null;
        try {
            responseData = server.serializeCommandData(commandResponse);
            logger.info("Данные десериализованы");
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
                CommandRequest request = receiveCommandRequest();
                Report report = getReport(request);
                CommandResponse commandResponse = getCommandResponse(report);
                byte[] responseData = serializeCommandResponse(commandResponse);
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
}
