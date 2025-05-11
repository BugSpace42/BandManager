package main.java.commands;

import main.java.utility.ExecutableCommand;
import utility.Report;

public class Save extends ExecutableCommand {
    public Save() {
        super("save", "сохранить коллекцию в файл",
                0, new String[]{});
    }

    /**
     * Выполняет команду.
     * @param args аргументы команды
     * @return отчёт о выполнении команды
     */
    @Override
    public Report execute(String[] args){
        // Очищаем коллекцию
        Report report = new Report(0, null, "Коллекция не очищена, команды нету");
        return report;
    }
}
