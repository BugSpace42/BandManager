package commands;

import managers.ConsoleManager;
import utility.Command;
import utility.Runner.ExitCode;

/**
 * Завершает программу без сохранения в файл.
 * @author Alina
 */
public class Exit extends Command{

    public Exit() {
        super("exit", "завершить программу (без сохранения в файл)", 0);
    }

    /**
     * Выполняет команду.
     */
    @Override
    public ExitCode execute(String[] args) {
        try {
            return ExitCode.EXIT;
        } catch (Exception e) {
            ConsoleManager.printError("Непредвиденная ошибка!");
            return ExitCode.ERROR;
        }
    }
}
