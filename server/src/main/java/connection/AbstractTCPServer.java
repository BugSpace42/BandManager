package main.java.connection;

import connection.requests.*;
import connection.responses.*;
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
    private final Logger logger = LogManager.getLogger();
    private final ServerSocket serverSocket;
    private Socket socket;

    private boolean running = false;

    public AbstractTCPServer(InetAddress addr, int port) throws IOException {
        this.addr = new InetSocketAddress(addr, port);
        this.serverSocket = new ServerSocket(port);
    }

    public byte[] receiveData(InputStream in) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = in.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        byte[] dataByteArray = byteArrayOutputStream.toByteArray();
        return dataByteArray;
    }

    public CommandRequest receiveCommandRequest() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        CommandRequest request = (CommandRequest) inputStream.readObject();
        return request;
    }

    public void sendData(byte[] data) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(data);
        out.flush();
    }

    public Socket connectToClient() throws IOException {
        return serverSocket.accept();
    }

    public void disconnectFromClient() throws IOException {
        socket.close();
    }

    public CommandRequest deserializeCommandRequest(byte[] data) throws IOException {
        CommandRequest request = SerializationUtils.deserialize(data);
        return request;
    }

    public ListOfCommandsResponse deserializeCommandList(byte[] data) throws IOException {
        ListOfCommandsResponse listOfCommandsResponse = SerializationUtils.deserialize(data);
        return listOfCommandsResponse;
    }

    public byte[] serializeCommandData(CommandResponse response) {
        byte[] data = SerializationUtils.serialize(response);
        return data;
    }

    public byte[] serializeListOfCommandsData(ListOfCommandsResponse response) {
        byte[] data = SerializationUtils.serialize(response);
        return data;
    }

    public Report handleRequest(CommandRequest request) {
        return CommandRequestManager.directCommand(request);
    }

    public CommandResponse formCommandResponse(Report report) {
        return new CommandResponse(report.getCode(), report.getError(), report.getMessage());
    }

    public ListOfCommandsResponse formListOfCommandsResponse(HashMap<String, Command> commands) {
        return new ListOfCommandsResponse(commands);
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    public void start() throws IOException {
        System.out.println("Информация о доступных командах ещё не отправлена клиенту");
        HashMap<String, Command> commands = CommandManager.getCommands();
        ListOfCommandsResponse listOfCommandsResponse = formListOfCommandsResponse(commands);
        byte[] data = serializeListOfCommandsData(listOfCommandsResponse);

        sendData(data);
        logger.info("Информация о доступных командах отправлена клиенту.");
        System.out.println("Информация о доступных командах отправлена клиенту");
    }

    public void run() {
        running = true;
        logger.info("Сервер запущен по адресу " + addr);

        logger.info("Ожидание подключения клиента на порт " + getPort());
        try {
            socket = connectToClient();
            logger.info("Клиент успешно подключился на порт "  + getPort());
        } catch (Exception e) {
            logger.error("Ошибка при подключении клиента", e);
            running = false;
        }

        try {
            start();
            logger.info("Произведены действия для начала работы с клиентом.");
        }
        catch (Exception e) {
            logger.error("Ошибка при произведении действий для начала работы с клиентом", e);
            running = false;
        }

        while (running) {
            CommandRequest request = null;
            try {
                request = receiveCommandRequest();
                logger.info("Запрос получен от клиента");
            } catch (Exception e) {
                logger.error("Ошибка при получении данных клиента", e);
                continue;
            }

            Report report = null;
            try {
                report = handleRequest(request);
                logger.info("Данные клиента обработаны.");
            } catch (Exception e) {
                logger.error("Ошибка при обработке данных клиента", e);
                continue;
            }

            CommandResponse commandResponse = null;
            try {
                commandResponse = formCommandResponse(report);
                logger.info("Сформирован ответ клиенту");
            } catch (Exception e) {
                logger.error("Ошибка при формировании ответа клиенту", e);
                continue;
            }

            byte[] responseData = null;
            try {
                responseData = serializeCommandData(commandResponse);
                logger.info("Данные десериализованы");
            } catch (Exception e) {
                logger.error("Ошибка сериализации ответа клиенту", e);
                continue;
            }

            try {
                sendData(responseData);
                logger.info("Данные отправлены клиенту");
            } catch (IOException e) {
                logger.error("Ошибка при отправке данных клиенту", e);
                continue;
            }
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
