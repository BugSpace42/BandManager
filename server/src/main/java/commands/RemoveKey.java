package main.java.commands;

import main.java.utility.ExecutableCommand;
import utility.Report;

public class RemoveKey extends ExecutableCommand {
    public RemoveKey() {
        super("remove_key", "удалить элемент из коллекции по его ключу",
                1, new String[]{});
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
