import commands.*;
import connection.TCPClient;
import managers.CommandManager;
import managers.ConsoleManager;
import managers.FileManager;
import utility.Runner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

        CommandManager commandManager = CommandManager.getCommandManager();
        
        String defaultFilePath = "collection.csv";
        Path filePath = Path.of(defaultFilePath);

        FileManager fileManager = new FileManager(filePath.toAbsolutePath());
        Runner.setFileManager(fileManager);
        Runner runner = Runner.getRunner();

        commandManager.newCommand(new ClientHelp());
        commandManager.newCommand(new ClientInfo());
        commandManager.newCommand(new ClientShow());
        commandManager.newCommand(new ClientInsert());
        commandManager.newCommand(new ClientUpdate());
        commandManager.newCommand(new ClientRemoveKey());
        commandManager.newCommand(new ClientClear());
        commandManager.newCommand(new ClientSave());
        commandManager.newCommand(new ClientExecuteScript());
        commandManager.newCommand(new ClientExit());
        commandManager.newCommand(new ClientRemoveGreater());
        commandManager.newCommand(new ClientHistory());
        commandManager.newCommand(new ClientReplaceIfGreater());
        commandManager.newCommand(new ClientRemoveAllByNumberOfParticipants());
        commandManager.newCommand(new ClientRemoveAnyByBestAlbum());
        commandManager.newCommand(new ClientPrintFieldDescendingNumberOfParticipants());

        try {
            InetAddress addr = InetAddress.getLocalHost();
            TCPClient client = new TCPClient(addr, port);

            runner.run();
        } catch (IOException e) {
            ConsoleManager.printError("Невозможно подключиться к серверу!");
        }
    }
}
