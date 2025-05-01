package commands;

import managers.ConsoleManager;
import utility.Runner;
import utility.ExitCode;

/**
 * Очищает коллекцию.
 * @author Alina
 */
public class ClientClear extends ClearCommand {
    /**
     * Выполняет команду.
     */
    public int execute(String[] args) {
        Runner runner = Runner.getRunner();
        try {
            runner.collectionManager.clearCollection();
            return ExitCode.OK.code;
        } catch (Exception e) {
            ConsoleManager.printError("Непредвиденная ошибка!");
            return ExitCode.ERROR.code;
        }
    }
}
