package main.java.commands;

import main.java.managers.CommandManager;
import main.java.utility.ExecutableCommand;
import main.java.utility.ExitCode;
import main.java.utility.Report;

import java.util.ArrayList;

/**
 * Выводит последние 8 команд (без их аргументов)
 * @author Alina
 */
public class History extends ExecutableCommand {
    public History(){
        super("history", "вывести последние 8 команд", 0, new String[]{});
    }

    /**
     * Выполняет команду.
     * @param args аргументы команды
     * @return отчёт о выполнении команды
     */
    @Override
    public Report execute(String[] args){
        Report report = null;
        try {
            String historyString = "";
            int numberOfCommands = 8;
            ArrayList<String> history = CommandManager.getCommandHistory();
            if (history.isEmpty()) {
                historyString = "История команд пуста.";
            }
            else {
                historyString = "История команд (начиная с последней):\n";
                for (int i = 0; i < Math.min(numberOfCommands, history.size()); i++) {
                    historyString += history.get(history.size()-i-1) + "\n";
                }
            }
            report = new Report(ExitCode.OK.code, null, historyString);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
