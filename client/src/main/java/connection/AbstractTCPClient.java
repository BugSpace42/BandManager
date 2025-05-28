package main.java.connection;

import connection.requests.CommandRequest;
import connection.requests.Request;
import connection.responses.IdListResponse;
import connection.responses.KeyListResponse;
import connection.responses.CommandMapResponse;
import main.java.managers.ConsoleManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.lang3.SerializationUtils;
import commands.Command;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AbstractTCPClient {
    private final InetSocketAddress addr;
    private final Logger logger = LogManager.getLogger("ClientLogger");
    private final SocketChannel channel;
    private final Selector selector;
    private final SelectionKey key;

    // Поля для отправки данных
    private ByteBuffer pendingData; // буфер с данными для отправки
    private boolean isSending = false; // флаг, что есть данные для отправки

    public AbstractTCPClient(InetAddress addr, int port, Selector selector) throws IOException {
        this.addr = new InetSocketAddress(addr, port);
        this.selector = selector;
        this.channel = SocketChannel.open();
        // Устанавливаем в неблокирующий режим
        this.channel.configureBlocking(false);
        // Подключаемся к серверу
        this.channel.connect(new InetSocketAddress("localhost", 12345));
        // Регистрируем канал в селекторе
        this.key = this.channel.register(selector,SelectionKey.OP_CONNECT);
        logger.info("Клиент подключен к " + addr);
    }

    public <T> T deserializeObject(byte[] dataBytes) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dataBytes));
        return (T) ois.readObject();
    }

    public <T> T receiveAndDeserialize(SocketChannel channel) throws IOException, ClassNotFoundException {
        byte[] dataBytes = readData(channel);
        return deserializeObject(dataBytes);
    }

    public byte[] readData(SocketChannel channel) throws IOException, ClassNotFoundException {
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
        return dataBytes;
    }

    /**
     * Подготавливает данные для отправки на сервер.
     * @param data данные для отправки на сервер
     */
    public void prepareData(byte[] data) {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        lengthBuffer.putInt(data.length);
        lengthBuffer.flip();

        // Объединяем длину и данные в один буфер
        ByteBuffer fullBuffer = ByteBuffer.allocate(4 + data.length);
        fullBuffer.put(lengthBuffer);
        fullBuffer.put(data);
        fullBuffer.flip();

        // Сохраняем в буфер для отправки
        this.pendingData = fullBuffer;
        this.isSending = true;

        // Регистрируем канал на OP_WRITE (если еще не зарегистрирован)
        SelectionKey key = channel.keyFor(selector);
        key.interestOps(SelectionKey.OP_WRITE | key.interestOps());
    }

    public void sendData(SelectionKey key) {
        try {
            if (pendingData != null) {
                channel.write(pendingData);
                if (!pendingData.hasRemaining()) {
                    logger.info("Все данные успешно отправлены на сервер.");
                    pendingData = null;
                    isSending = false;
                    key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                }
            }
        } catch (IOException e) {
            logger.error("При отправлении данных на сервер произошла ошибка: {}", e.getMessage());
        }
    }

    public HashMap<String, Command> getCommandMap() throws IOException, ClassNotFoundException {
        // Ждем, пока канал не станет готов к чтению
        while (true) {
            int readyChannels = selector.select(); // блокирует до события
            if (readyChannels == 0){
                continue; // если ничего не готово, повторяем
            }

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey selKey = keyIterator.next();
                if (selKey.isReadable()) {
                    // Канал готов к чтению
                    CommandMapResponse response = receiveAndDeserialize(channel);
                    HashMap<String, Command> commands = response.getCommandMap();
                    logger.info("Получен список команд от сервера: " + commands.size() + " команд.");
                    keyIterator.remove(); // удаляем обработанный ключ
                    return commands;
                }
                keyIterator.remove(); // удаляем обработанный ключ
            }
        }
    }

    public List<Integer> getKeyList() throws IOException, ClassNotFoundException {
        // Ждем, пока канал не станет готов к чтению
        while (true) {
            int readyChannels = selector.select(); // блокирует до события
            if (readyChannels == 0) continue; // если ничего не готово, повторяем

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey selKey = keyIterator.next();
                if (selKey.isReadable()) {
                    // Канал готов к чтению
                    KeyListResponse response = receiveAndDeserialize(channel);
                    List<Integer> keyList = response.getKeyList();
                    logger.info("Получен список ключей объектов: " + keyList.size() + " ключей.");
                    keyIterator.remove(); // удаляем обработанный ключ
                    return keyList;
                }
                keyIterator.remove(); // удаляем обработанный ключ
            }
        }
    }

    public List<Long> getIdList() throws IOException, ClassNotFoundException {
        // Ждем, пока канал не станет готов к чтению
        while (true) {
            int readyChannels = selector.select(); // блокирует до события
            if (readyChannels == 0) continue; // если ничего не готово, повторяем

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey selKey = keyIterator.next();
                if (selKey.isReadable()) {
                    // Канал готов к чтению
                    IdListResponse response = receiveAndDeserialize(channel);
                    List<Long> idList = response.getIdList();
                    logger.info("Получен список id объектов: " + idList.size() + " id.");
                    keyIterator.remove(); // удаляем обработанный ключ
                    return idList;
                }
                keyIterator.remove(); // удаляем обработанный ключ
            }
        }
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

    public Selector getSelector() {
        return selector;
    }

    public boolean isSending() {
        return isSending;
    }
}
