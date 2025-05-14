package main.java.commands;

import utility.ExecutableCommand;
import utility.ExitCode;
import utility.Report;

/**
 * Завершает программу без сохранения в файл.
 * @author Alina
 */
public class Exit extends ExecutableCommand {
    public Exit() {
        super("exit", "завершить программу (без сохранения в файл)",
                0, new String[]{});
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
