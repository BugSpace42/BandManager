import commands.*;
import managers.CommandManager;
import managers.ConsoleManager;
import managers.FileManager;
import utility.Runner;

import java.nio.file.Path;

/**
 * Основной класс.
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

        commandManager.newCommand(new Help());
        commandManager.newCommand(new Info());
        commandManager.newCommand(new Show());
        commandManager.newCommand(new Insert());
        commandManager.newCommand(new Update());
        commandManager.newCommand(new RemoveKey());
        commandManager.newCommand(new Clear());
        commandManager.newCommand(new Save());
        commandManager.newCommand(new ExecuteScript());
        commandManager.newCommand(new Exit());
        commandManager.newCommand(new RemoveGreater());
        commandManager.newCommand(new History());
        commandManager.newCommand(new ReplaceIfGreater());
        commandManager.newCommand(new RemoveAllByNumberOfParticipants());
        commandManager.newCommand(new RemoveAnyByBestAlbum());
        commandManager.newCommand(new PrintFieldDescendingNumberOfParticipants());
        
        runner.run();
    }
}
