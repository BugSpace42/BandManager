package commands;

import managers.ConsoleManager;
import utility.Command;
import utility.Runner;
import utility.Runner.ExitCode;

import java.io.IOException;

/**
 * Сохраняет коллекцию в файл.
 * @author Alina
 */
public class Save extends Command{
    public Save() {
        super("save", "сохранить коллекцию в файл", 0);
    }

    /**
     * Выполняет команду.
     */
    @Override
    public ExitCode execute(String[] args) {
        Runner runner = Runner.getRunner();
        try {
            runner.fileManager.writeCollection(runner.collectionManager.getCollection());
        } catch (IOException e) {
            ConsoleManager.printError("Невозможно сохранить коллекцию в файл.");
            return ExitCode.ERROR;
        }
        return ExitCode.OK;
    }
}
