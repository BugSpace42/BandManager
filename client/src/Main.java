import connection.TCPClient;
import managers.ConsoleManager;
import managers.FileManager;
import utility.Runner;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;

/**
 * Основной класс.
 * @author Alina
 */
public class Main {
    /**
     * Основной метод
     * @param args название файла с загружаемой коллекцией
     */
    public static void main(String[] args) {
        int port = 12345;

        Runner runner = Runner.getRunner();

        try {
            InetAddress addr = InetAddress.getLocalHost();
            TCPClient client = new TCPClient(addr, port);
            runner.setClient(client);

            runner.run();
        } catch (IOException e) {
            ConsoleManager.printError("Невозможно подключиться к серверу!");
        }
    }
}
