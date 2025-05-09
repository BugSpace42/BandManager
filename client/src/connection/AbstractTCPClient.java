package connection;

import connection.requests.CommandRequest;
import connection.requests.Request;
import connection.responses.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public abstract class AbstractTCPClient {
    private final InetSocketAddress addr;
    private final Logger logger = LogManager.getLogger("ClientLogger");
    private Socket socket;

    public AbstractTCPClient(InetAddress addr, int port) throws IOException {
        this.addr = new InetSocketAddress(addr, port);
        this.socket = new Socket(addr, port);
        logger.info("Клиент подключен к " + addr);
    }

    public byte[] receiveData(InputStream in) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = in.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        byte[] dataByteArray = byteArrayOutputStream.toByteArray();
        logger.info("От сервера получены данные, длиной: " + dataByteArray.length);
        return dataByteArray;
    }

    public void sendData(byte[] data) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(data);
        out.flush();
        logger.info("Данные отправлены на сервер.");
    }

    public Response deserializeData(byte[] data) throws IOException {
        Response response = SerializationUtils.deserialize(data);
        logger.info("Ответ от сервера десериализован.");
        return response;
    }

    public byte[] serializeData(Request request) throws IOException {
        byte[] data = SerializationUtils.serialize(request);
        logger.info("Запрос к серверу сериализован.");
        return data;
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
