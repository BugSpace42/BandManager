package main.java.commands;

import main.java.utility.ExecutableCommand;
import utility.Report;

public class Update extends ExecutableCommand {
    public Update() {
        super("update",
                "обновить значение элемента коллекции, id которого равен заданному",
                1, new String[]{"MusicBand"});
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
