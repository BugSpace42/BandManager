package main.java.connection;

import connection.requests.*;
import connection.responses.*;
import main.java.handlers.ClientHandler;
import main.java.managers.CollectionManager;
import main.java.managers.CommandManager;
import main.java.managers.CommandRequestManager;
import commands.Command;
import commands.Report;

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

    private boolean running = false;

    public AbstractTCPServer(InetAddress addr, int port) throws IOException {
        this.addr = new InetSocketAddress(addr, port);
        this.serverSocket = new ServerSocket(port);
    }

    public <T> T receiveObject(Socket socket) throws IOException, ClassNotFoundException {
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        int dataLength = dataInputStream.readInt();
        logger.info("Длина данных считана: {}", dataLength);

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

    public void sendData(Socket socket, byte[] data) throws IOException {
        OutputStream out = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(data.length);
        out.write(data);
        out.flush();
    }

    public Socket connectToClient() throws IOException {
        return serverSocket.accept();
    }

    public void disconnectFromClient(Socket socket) throws IOException {
        socket.close();
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

    public void run() {
        running = true;
        logger.info("Сервер запущен по адресу {}", addr);
        try {
            while (running) {
                logger.info("Ожидание подключения клиента на порт {}", getPort());
                Socket clientSocket;
                clientSocket = connectToClient();
                logger.info("Клиент успешно подключился на порт {}", getPort());
                new Thread(new ClientHandler(this, clientSocket)).start();

                // Выход из цикла для отладки
                //running = false;
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
