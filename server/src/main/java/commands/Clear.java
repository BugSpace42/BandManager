package main.java.commands;

import main.java.managers.CollectionManager;
import main.java.utility.ExecutableCommand;
import utility.ExitCode;
import utility.Report;

/**
 * Очищает коллекцию.
 * @author Alina
 */
public class Clear extends ExecutableCommand {
    public Clear() {
        super("clear", "очистить коллекцию", 0, new String[]{});
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
            CollectionManager collectionManager = CollectionManager.getCollectionManager();
            collectionManager.clearCollection();
            String message = "Коллекция очищена.";
            report = new Report(ExitCode.OK.code, null, message);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
