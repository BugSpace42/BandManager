package main.java.connection;

import connection.requests.CommandRequest;
import connection.responses.Response;
import main.java.managers.CommandRequestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.net.*;

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

    public void sendData(byte[] data) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(data);
    }

    public Socket connectToClient() throws IOException {
        return serverSocket.accept();
    }

    public void disconnectFromClient() throws IOException {
        socket.close();
    }

    public CommandRequest deserializeData(byte[] data) throws IOException {
        CommandRequest request = SerializationUtils.deserialize(data);
        return request;
    }

    public byte[] serializeData(Response response) {
        byte[] data = SerializationUtils.serialize(response);
        return data;
    }

    public int handleRequest(CommandRequest request) {
        return CommandRequestManager.directCommand(request);
    }

    public Response formResponse(int code, String error) {
        Response response = new Response(code, error);
        return response;
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    public void run() {
        running = true;
        logger.info("Сервер запущен по адресу " + addr);

        while (running) {
            logger.info("Ожидание подключения клиента на порт " + getPort());
            try {
                socket = connectToClient();
                logger.info("Клиент успешно подключился на порт "  + getPort());
            } catch (Exception e) {
                logger.error("Ошибка при подключении клиента", e);
                continue;
            }

            InputStream in = null;
            byte[] data = null;
            try {
                in = socket.getInputStream();
                data = receiveData(in);
                logger.info("Данные успешно получены");
            } catch (Exception e) {
                logger.error("Ошибка при получении данных от клиента", e);
                continue;
            }

            CommandRequest request = null;
            try {
                request = deserializeData(data);
                logger.info("Данные десериализованы");
            } catch (Exception e) {
                logger.error("Ошибка при десериализации данных клиента", e);
                continue;
            }

            int exitCode = -1;
            try {
                exitCode = handleRequest(request);
            } catch (Exception e) {
                logger.error("Ошибка при обработке данных клиента", e);
                continue;
            }

            Response response = null;
            try {
                response = formResponse(0, null);
                // TODO формирование ответа клиенту
                logger.info("Сформирован ответ клиенту");
            } catch (Exception e) {
                logger.error("Ошибка при формировании ответа клиенту", e);
                continue;
            }

            byte[] responseData = null;
            try {
                responseData = serializeData(response);
                logger.info("Данные десериализованы");
            } catch (Exception e) {
                logger.error("Ошибка сериализации ответа клиенту", e);
                continue;
            }

            OutputStream out = null;
            try {
                out = socket.getOutputStream();
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
