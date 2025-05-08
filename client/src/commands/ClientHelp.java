package commands;

import managers.ConsoleManager;
import utility.Command;
import utility.ExitCode;
import utility.Runner;

/**
 * Выводит справку по доступным командам.
 * @author Alina
 */
public class ClientHelp extends HelpCommand {
    /**
     * Выполняет команду.
     */
    public int execute(String[] args){
        Runner runner = Runner.getRunner();
        try {
            ConsoleManager.println("Доступные команды:");
            for (Command command : runner.commandManager.getCommands().values()) {
                ConsoleManager.println(command.getName() + ": " + command.getDescription());
            }
            return ExitCode.OK.code;
        } catch (Exception e) {
            ConsoleManager.printError("Непредвиденная ошибка!");
            return ExitCode.ERROR.code;
        }
    }
}
