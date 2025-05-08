package commands;

import managers.CollectionManager;
import managers.ConsoleManager;
import utility.ExitCode;
import utility.Runner;

import java.io.IOException;

/**
 * Сохраняет коллекцию в файл.
 * @author Alina
 */
public class ClientSave extends SaveCommand {
    /**
     * Выполняет команду.
     */
    @Override
    public int execute(String[] args) {
        try {
            Runner.fileManager.writeCollection(CollectionManager.getCollection());
        } catch (IOException e) {
            ConsoleManager.printError("Невозможно сохранить коллекцию в файл.");
            return ExitCode.ERROR.code;
        }
        return ExitCode.OK.code;
    }
}
