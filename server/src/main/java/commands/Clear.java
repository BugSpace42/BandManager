package main.java.commands;

import main.java.managers.CollectionManager;
import commands.ExecutableCommand;
import utility.ExitCode;
import commands.Report;
import utility.Types;

/**
 * Очищает коллекцию.
 * @author Alina
 */
public class Clear extends ExecutableCommand {
    public Clear() {
        super("clear", "очистить коллекцию", new Types[]{}, new Types[]{});
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
