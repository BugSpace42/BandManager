package main.java.commands;

import main.java.utility.ExecutableCommand;
import utility.Report;

public class RemoveAnyByBestAlbum extends ExecutableCommand {
    public RemoveAnyByBestAlbum() {
        super("remove_any_by_best_album",
                "удалить из коллекции один элемент, значение поля bestAlbum которого эквивалентно заданному",
                0, new String[]{"Album"});
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
