package commands;

import managers.ConsoleManager;
import utility.ExitCode;

/**
 * Завершает программу без сохранения в файл.
 * @author Alina
 */
public class ClientExit extends ExitCommand{
    /**
     * Выполняет команду.
     */
    @Override
    public int execute(String[] args) {
        try {
            return ExitCode.EXIT.code;
        } catch (Exception e) {
            ConsoleManager.printError("Непредвиденная ошибка!");
            return ExitCode.ERROR.code;
        }
    }
}
