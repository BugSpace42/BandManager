package main.java.commands;

import main.java.utility.ExecutableCommand;
import utility.Report;

public class ReplaceIfGreater extends ExecutableCommand {
    public ReplaceIfGreater() {
        super("replace_if_greater",
                "заменить значение по ключу, если новое значение больше старого",
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
