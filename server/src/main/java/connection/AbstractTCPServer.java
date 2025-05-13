package main.java.connection;

import main.java.connection.requests.*;
import main.java.connection.responses.*;
import main.java.managers.CommandManager;
import main.java.managers.CommandRequestManager;
import main.java.utility.Command;
import main.java.utility.Report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public abstract class AbstractTCPServer {
    private final InetSocketAddress addr;
    private static final Logger logger = LogManager.getLogger(AbstractTCPServer.class);
    private final ServerSocket serverSocket;
    private Socket socket;

    private boolean running = false;
    private boolean connection = false;

    public AbstractTCPServer(InetAddress addr, int port) throws IOException {
        this.addr = new InetSocketAddress(addr, port);
        this.serverSocket = new ServerSocket(port);
    }

    public CommandRequest receiveCommandRequest() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        return (CommandRequest) inputStream.readObject();
    }

    public void sendData(byte[] data) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(data);
        out.flush();
    }

    public Socket connectToClient() throws IOException {
        connection = true;
        return serverSocket.accept();
    }

    public void disconnectFromClient() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connection = false;
    }

    public byte[] serializeCommandData(CommandResponse response) {
        return SerializationUtils.serialize(response);
    }

    public byte[] serializeListOfCommandsData(ListOfCommandsResponse response) {
        return SerializationUtils.serialize(response);
    }

    public Report handleRequest(CommandRequest request) {
        CommandManager.addToHistory(request.getName());
        return CommandRequestManager.directCommand(request);
    }

    public Report executeServerCommand(String[] args) {
        CommandRequest request = new CommandRequest(args[0], args);
        return CommandRequestManager.directServerCommand(request);
    }

    public CommandResponse formCommandResponse(Report report) {
        return new CommandResponse(report.getCode(), report.getError(), report.getMessage());
    }

    public ListOfCommandsResponse formListOfCommandsResponse(HashMap<String, Command> commands) {
        return new ListOfCommandsResponse(commands);
    }

    public void saveCollection() {
        executeServerCommand(new String[]{"save"});
    }

    public void close() throws IOException {
        saveCollection();
        serverSocket.close();
    }

    public void start() throws IOException {
        HashMap<String, Command> commands = CommandManager.getCommands();
        ListOfCommandsResponse listOfCommandsResponse = formListOfCommandsResponse(commands);
        byte[] data = serializeListOfCommandsData(listOfCommandsResponse);

        sendData(data);
        logger.info("Информация о доступных командах отправлена клиенту.");
    }

    public void run() {
        running = true;
        logger.info("Сервер запущен по адресу " + addr);

        while (running) {
            logger.info("Ожидание подключения клиента на порт " + getPort());
            try {
                socket = connectToClient();
                logger.info("Клиент успешно подключился на порт " + getPort());
            } catch (Exception e) {
                logger.error("Ошибка при подключении клиента", e);
                disconnectFromClient();
            }

            try {
                start();
                logger.info("Произведены действия для начала работы с клиентом.");
            } catch (Exception e) {
                logger.error("Ошибка при произведении действий для начала работы с клиентом", e);
                disconnectFromClient();
            }

            while (connection) {
                CommandRequest request = null;
                try {
                    request = receiveCommandRequest();
                    logger.info("Запрос получен от клиента");
                } catch (Exception e) {
                    logger.error("Ошибка при получении данных клиента", e);
                    disconnectFromClient();
                    continue;
                }

                Report report = null;
                try {
                    report = handleRequest(request);
                    logger.info("Данные клиента обработаны.");
                } catch (Exception e) {
                    logger.error("Ошибка при обработке данных клиента", e);
                    disconnectFromClient();
                    continue;
                }

                CommandResponse commandResponse = null;
                try {
                    commandResponse = formCommandResponse(report);
                    logger.info("Сформирован ответ клиенту");
                } catch (Exception e) {
                    logger.error("Ошибка при формировании ответа клиенту", e);
                    disconnectFromClient();
                    continue;
                }

                byte[] responseData = null;
                try {
                    responseData = serializeCommandData(commandResponse);
                    logger.info("Данные десериализованы");
                } catch (Exception e) {
                    logger.error("Ошибка сериализации ответа клиенту", e);
                    disconnectFromClient();
                    continue;
                }

                try {
                    sendData(responseData);
                    logger.info("Данные отправлены клиенту");
                } catch (IOException e) {
                    logger.error("Ошибка при отправке данных клиенту", e);
                    disconnectFromClient();
                    continue;
                }
            }
            // отладочная строчка
            running = false;
        }
        try {
            close();
        } catch (IOException e) {
            logger.error("Ошибка при закрытии сервера", e);
        }
    }

    public InetSocketAddress getSocketAddress() {
        return addr;
    }

    public InetAddress getInetAddress() {
        return addr.getAddress();
    }

    public int getPort() {
        return addr.getPort();
    }
}
