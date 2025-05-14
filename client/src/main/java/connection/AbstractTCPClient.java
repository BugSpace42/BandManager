package main.java.connection;

import connection.requests.CommandRequest;
import connection.requests.Request;
import connection.responses.ListOfCommandsResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.lang3.SerializationUtils;
import utility.Command;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public abstract class AbstractTCPClient {
    private final InetSocketAddress addr;
    private final Logger logger = LogManager.getLogger("ClientLogger");
    private final SocketChannel channel;

    public AbstractTCPClient(InetAddress addr, int port) throws IOException {
        this.addr = new InetSocketAddress(addr, port);
        this.channel = SocketChannel.open(this.addr);
        logger.info("Клиент подключен к " + addr);
    }

    public <T> T deserializeObject(byte[] dataBytes) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dataBytes));
        return (T) ois.readObject();
    }

    public <T> T receiveAndDeserialize() throws IOException, ClassNotFoundException {
        ByteBuffer lengthBuf = ByteBuffer.allocate(4);
        while (lengthBuf.hasRemaining()) {
            channel.read(lengthBuf);
        }
        lengthBuf.flip();
        int dataLength = lengthBuf.getInt();
        logger.info("Считана длина данных: " + dataLength);

        ByteBuffer dataBuf = ByteBuffer.allocate(dataLength);
        while (dataBuf.hasRemaining()) {
            channel.read(dataBuf);
        }
        byte[] dataBytes = dataBuf.array();
        logger.info("Данные успешно считаны.");

        return deserializeObject(dataBytes);
    }

    public void sendData(byte[] data) throws IOException {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        lengthBuffer.putInt(data.length);
        lengthBuffer.flip();
        while (lengthBuffer.hasRemaining()) {
            channel.write(lengthBuffer);
        }
        logger.info("Длина данных успешно передана на сервер.");

        ByteBuffer dataBuffer = ByteBuffer.wrap(data);
        while (dataBuffer.hasRemaining()) {
            channel.write(dataBuffer);
        }
        logger.info("Данные успешно переданы на сервер.");
    }

    public HashMap<String, Command> getCommandMap() throws IOException, ClassNotFoundException {
        ListOfCommandsResponse response = receiveAndDeserialize();
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

    public void close() throws IOException {
        channel.close();
        logger.info("Канал закрыт.");
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

    public SocketChannel getSocketChannel() {
        return channel;
    }
}
