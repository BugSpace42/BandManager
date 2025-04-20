package commands;

import managers.ConsoleManager;
import utility.Command;
import utility.Runner;
import utility.Runner.ExitCode;

/**
 * Очищает коллекцию.
 * @author Alina
 */
public class Clear extends Command{

    public Clear() {
        super("clear", "очистить коллекцию", 0);
    }

    /**
     * Выполняет команду.
     */
    @Override
    public ExitCode execute(String[] args) {
        Runner runner = Runner.getRunner();
        try {
            runner.collectionManager.clearCollection();
            return ExitCode.OK;
        } catch (Exception e) {
            ConsoleManager.printError("Непредвиденная ошибка!");
            return ExitCode.ERROR;
        }
    }
}
