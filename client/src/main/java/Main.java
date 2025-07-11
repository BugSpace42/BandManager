package main.java;

import main.java.commands.ClientExecuteScript;
import main.java.connection.TCPClient;
import main.java.managers.ConsoleManager;
import main.java.managers.Runner;
import connection.SSHPortForwarding;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.Selector;

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
        Runner runner = Runner.getRunner();
        runner.addClientCommand(new ClientExecuteScript());

        // Подключение локально
        int port = 53621;

        // Подключение по SSH
        //int port = SSHPortForwarding.getLocalPort();
        //SSHPortForwarding.connect();

        try {
            InetAddress addr = InetAddress.getByName("localhost");
            Selector selector = Selector.open();
            TCPClient client = new TCPClient(addr, port, selector);
            runner.setClient(client);
            runner.setSelector(selector);
            ConsoleManager.println("Успешное подключение к серверу.");
            runner.run();
        } catch (IOException e) {
            ConsoleManager.printError("Невозможно подключиться к серверу!");
            ConsoleManager.printError(e);
        } finally {
            SSHPortForwarding.disconnect();
        }
    }
}
