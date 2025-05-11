package connection;

import connection.requests.*;
import connection.responses.*;
import managers.ConsoleManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.lang3.SerializationUtils;
import utility.Command;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTCPClient {
    private final InetSocketAddress addr;
    private final Logger logger = LogManager.getLogger("ClientLogger");
    private Socket socket;

    public AbstractTCPClient(InetAddress addr, int port) throws IOException {
        this.addr = new InetSocketAddress(addr, port);
        this.socket = new Socket(addr, port);
        logger.info("Клиент подключен к " + addr);
    }

    public byte[] receiveData() throws IOException {
        InputStream in = socket.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(in);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while (bis.available() > 0) {
            bytesRead = bis.read(buffer);
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        byte[] dataByteArray = byteArrayOutputStream.toByteArray();
        logger.info("От сервера получены данные, длиной: " + dataByteArray.length);
        return dataByteArray;
    }

    public CommandResponse receiveCommandResponse() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        CommandResponse response = (CommandResponse) inputStream.readObject();
        logger.info("От сервера получен ответ о выполнении команды.");
        return response;
    }

    public ListOfCommandsResponse receiveListOfCommandResponse() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        ListOfCommandsResponse response = (ListOfCommandsResponse) inputStream.readObject();
        logger.info("От сервера получены данные о доступных командах.");
        return response;
    }

    public void sendData(byte[] data) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(data);
        out.flush();
        logger.info("Данные отправлены на сервер.");
    }

    public CommandResponse deserializeCommandData(byte[] data) throws IOException {
        CommandResponse commandResponse = SerializationUtils.deserialize(data);
        logger.info("Ответ от сервера десериализован.");
        return commandResponse;
    }

    public ListOfCommandsResponse deserializeListOfCommandsData(byte[] data) throws IOException {
        ListOfCommandsResponse listOfCommandsResponse = SerializationUtils.deserialize(data);
        logger.info("Список команд от сервера десериализован.");
        return listOfCommandsResponse;
    }

    public HashMap<String, Command> getCommandMap() throws IOException, ClassNotFoundException {
        ListOfCommandsResponse response = receiveListOfCommandResponse();
        HashMap<String, Command> commands = response.getCommands();
        logger.info("Получен список команд от сервера: " + commands.size() + " команд.");
        return commands;
    }

    public byte[] serializeData(Request request) throws IOException {
        byte[] data = SerializationUtils.serialize(request);
        logger.info("Запрос к серверу сериализован.");
        return data;
    }

    public CommandRequest formRequest(String[] args) {
        CommandRequest request = new CommandRequest(args[0], args);
        return request;
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

    public Socket getSocket() {
        return socket;
    }
}
