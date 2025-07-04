package main.java.commands;

import main.java.managers.CommandManager;
import commands.ExecutableCommand;
import main.java.utility.Commands;
import utility.ExitCode;
import commands.Report;
import utility.Types;

import java.util.ArrayList;

/**
 * Выводит последние 8 команд (без их аргументов)
 * @author Alina
 */
public class History extends ExecutableCommand {
    public History(){
        super(Commands.HISTORY, "вывести последние 8 команд",
                new Types[]{}, new Types[]{});
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
            String errorString = "Непредвиденная ошибка: " + e.getMessage();
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
