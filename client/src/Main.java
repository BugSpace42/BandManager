import commands.*;
import managers.CommandManager;
import managers.ConsoleManager;
import managers.FileManager;
import utility.Runner;

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
        CommandManager commandManager = CommandManager.getCommandManager();
        
        String defaultFilePath = "collection.csv";
        Path filePath;
        if (args.length == 0) {
            ConsoleManager.println("Внимание! Не введено название файла с загружаемой коллекцией.");
            ConsoleManager.println("Будет загружена коллекция по умолчанию из файла " + defaultFilePath);
            filePath = Path.of(defaultFilePath);
        }
        else {
            filePath = Path.of(args[0]);
        }

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
        
        runner.run();
    }
}
