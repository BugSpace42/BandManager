package main.java.commands;

import commands.Command;
import commands.ExecutableCommand;
import commands.Report;
import main.java.managers.CommandManager;
import main.java.utility.Commands;
import utility.*;

import java.util.HashMap;

/**
 * Выводит справку по доступным командам.
 * @author Alina
 */
public class Help extends ExecutableCommand {
    public Help(){
        super(Commands.HELP, "вывести справку по доступным командам",
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
            String helpString = "Доступные команды:\n";
            HashMap<String, Command> commands = CommandManager.getCommands();
            for (String commandName : commands.keySet()){
                helpString = helpString + commandName + "\n";
            }
            report = new Report(ExitCode.OK.code, null, helpString);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}

/*
Шаблон:

        Report report;
        try {
            String message = "";
            report = new Report(ExitCode.OK.code, null, message);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;

*/