package main.java.commands;

import commands.ExecutableCommand;
import main.java.utility.Commands;
import utility.ExitCode;
import commands.Report;
import utility.Types;

/**
 * Завершает программу без сохранения в файл.
 * @author Alina
 */
public class Exit extends ExecutableCommand {
    public Exit() {
        super(Commands.EXIT, "завершить программу (без сохранения в файл)",
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
            report = new Report(ExitCode.EXIT.code, null, null);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
