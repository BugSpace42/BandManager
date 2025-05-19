package main.java.connection;

import connection.requests.*;
import connection.responses.*;
import main.java.managers.CollectionManager;
import main.java.managers.CommandManager;
import main.java.managers.CommandRequestManager;
import utility.Command;
import utility.Report;

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
    // По заданию, для обмена данными на сервере необходимо использовать потоки ввода-вывода
    // Поэтому я создаю сокеты и использую InputStream и OutputStream

    private boolean running = false;
    private boolean connection = false;
    private final CollectionManager collectionManager = CollectionManager.getCollectionManager();

    public AbstractTCPServer(InetAddress addr, int port) throws IOException {
        this.addr = new InetSocketAddress(addr, port);
        this.serverSocket = new ServerSocket(port);
    }

    public <T> T receiveObject() throws IOException, ClassNotFoundException {
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        int dataLength = dataInputStream.readInt();
        logger.info("Длина данных считана: " + dataLength);

        if (dataLength <= 0) {
            throw new IOException("Некорректная длина данных: " + dataLength);
        }

        byte[] dataBytes = new byte[dataLength];
        dataInputStream.readFully(dataBytes);
        logger.info("Данные успешно считаны");

        try (ByteArrayInputStream bais = new ByteArrayInputStream(dataBytes);
            ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        }
    }

    public void sendData(byte[] data) throws IOException {
        OutputStream out = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(data.length);
        out.write(data);
        out.flush();
    }

    public Socket connectToClient() throws IOException {
        connection = true;
        return serverSocket.accept();
    }

    public void disconnectFromClient() throws IOException {
        socket.close();
        connection = false;
    }

    public byte[] serializeCommandData(CommandResponse response) {
        return SerializationUtils.serialize(response);
    }

    public byte[] serializeResponse(Response response) {
        return SerializationUtils.serialize(response);
    }

    public Report handleRequest(CommandRequest request) {
        CommandManager.addToHistory(request.getName());
        return CommandRequestManager.directCommand(request);
    }

    public static Report executeServerCommand(String[] args) {
        CommandRequest request = new CommandRequest(args[0], args);
        return CommandRequestManager.directServerCommand(request);
    }

    public CommandResponse formCommandResponse(Report report) {
        return new CommandResponse(report.getCode(), report.getError(), report.getMessage());
    }

    public KeyListResponse formKeyListResponse() {
        return new KeyListResponse(CollectionManager.getKeyList());
    }

    public IdListResponse formIdListResponse() {
        return new IdListResponse(CollectionManager.getIdList());
    }

    public CommandMapResponse formListOfCommandsResponse(HashMap<String, Command> commands) {
        return new CommandMapResponse(commands);
    }

    public static void saveCollection() {
        executeServerCommand(new String[]{"save"});
    }

    public void close() {
        saveCollection();
        try {
            serverSocket.close();
            logger.info("Сервер успешно закрыт.");
        } catch (IOException e) {
            logger.error("Ошибка при закрытии сервера", e);
            logger.info("Принудительное завершение работы сервера.");
            System.exit(0);
        }
    }

    public void start() throws IOException {
        HashMap<String, Command> commands = CommandManager.getCommands();
        CommandMapResponse commandsMapResponse = formListOfCommandsResponse(commands);
        byte[] dataCommands = serializeResponse(commandsMapResponse);
        sendData(dataCommands);
        logger.info("Информация о доступных командах отправлена клиенту.");

        KeyListResponse keyListResponse = formKeyListResponse();
        byte[] dataKeys = serializeResponse(keyListResponse);
        sendData(dataKeys);
        logger.info("Информация о ключах элементов коллекции отправлена клиенту.");

        IdListResponse idListResponse = formIdListResponse();
        byte[] dataId = serializeResponse(idListResponse);
        sendData(dataId);
        logger.info("Информация о id элементов коллекции отправлена клиенту.");
    }

    public void run() {
        running = true;
        logger.info("Сервер запущен по адресу " + addr);
        try {
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
                        request = receiveObject();
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

                running = false; // отладочная строчка
            }
        } catch (Exception e) {
            logger.error("Ошибка при работе сервера", e);
        } finally {
            close();
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
