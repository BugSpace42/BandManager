package commands;

import managers.ConsoleManager;
import utility.ExitCode;
import utility.Runner;

import java.util.ArrayList;

/**
 * Выводит последние 8 команд (без их аргументов)
 * @author Alina
 */
public class ClientHistory extends HistoryCommand{
    /**
     * Выполняет команду.
     */
    @Override
    public int execute(String[] args){
        Runner runner = Runner.getRunner();
        try {
            int numberOfCommands = 8;
            ArrayList<String> history = runner.commandManager.getCommandHistory();
            if (history.isEmpty()) {
                ConsoleManager.println("История команд пуста.");
                return ExitCode.OK.code;
            }
            ConsoleManager.println("История команд (начиная с последней):");
            for (int i = 0; i < Math.min(numberOfCommands, history.size()); i++) {
                ConsoleManager.println(history.get(history.size()-i-1));
            }
            return ExitCode.OK.code;
        } catch (Exception e) {
            ConsoleManager.printError("Непредвиденная ошибка!");
            return ExitCode.ERROR.code;
        }
    }
}
