package main.java.commands;

import main.java.utility.ExecutableCommand;
import utility.Report;

public class ServerClear extends ExecutableCommand {
    public ServerClear() {
        super("clear", "очистить коллекцию", 0, new String[]{});
    }

    /**
     * Выполняет команду.
     * @param args аргументы команды
     * @return отчёт о выполнении команды
     */
    public Report execute(String[] args){
        // Очищаем коллекцию
        Report report = new Report(0, null, "Коллекция не очищена, команды нету");
        return report;
    }
}
