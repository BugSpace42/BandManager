package main.java;

import main.java.commands.ClientExecuteScript;
import main.java.connection.TCPClient;
import main.java.managers.ConsoleManager;
import main.java.managers.Runner;
import main.java.connection.SSHPortForwarding;

import java.io.IOException;
import java.net.InetAddress;

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

        int port = SSHPortForwarding.getLocalPort();
        SSHPortForwarding.connect();

        try {
            InetAddress addr = InetAddress.getByName("localhost");
            TCPClient client = new TCPClient(addr, port);
            runner.setClient(client);

            runner.run();
        } catch (IOException e) {
            ConsoleManager.printError("Невозможно подключиться к серверу!");
            ConsoleManager.printError(e);
        } finally {
            SSHPortForwarding.disconnect();
        }
    }
}
