package main.java.connection;

import connection.requests.Request;
import connection.responses.Response;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.lang3.SerializationUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    public abstract byte[] receiveData() throws IOException;

    public abstract void sendData(byte[] data, SocketAddress addr) throws IOException;

    public Socket connectToClient() throws IOException {
        return serverSocket.accept();
    }

    public void disconnectFromClient() throws IOException {
        socket.close();
    }

    public Request deserializeData(byte[] data) {
        Request request = SerializationUtils.deserialize(data);
        return request;
    }

    public byte[] serializeData(Response response) {
        byte[] data = SerializationUtils.serialize(response);
        return data;
    }

    public void handleRequest(Request request) {
        // todo
    }

    public abstract Response formResponse();

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
                in = new BufferedInputStream(socket.getInputStream());
                data = receiveData();
                logger.info("Данные успешно получены");
            } catch (Exception e) {
                logger.error("Ошибка при получении данных от клиента", e);
                continue;
            }

            Request request = null;
            try {
                request = deserializeData(data);
                logger.info("Данные десериализованы");
            } catch (Exception e) {
                logger.error("Ошибка при десериализации данных клиента", e);
                continue;
            }

            try {
                handleRequest(request);
            } catch (Exception e) {
                logger.error("Ошибка при обработке данных клиента", e);
                continue;
            }

            Response response = null;
            try {
                response = formResponse();
                // TODO
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
                //TODO sendData(responseData, addr);
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
