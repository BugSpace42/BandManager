package main.java.commands;

import main.java.utility.ExecutableCommand;
import utility.Report;

public class RemoveGreater extends ExecutableCommand {
    public RemoveGreater() {
        super("remove_greater",
                "удалить из коллекции все элементы, превышающие заданный",
                0, new String[]{"MusicBand"});
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
