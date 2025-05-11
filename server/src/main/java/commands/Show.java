package main.java.commands;

import main.java.managers.CollectionManager;
import main.java.utility.ExecutableCommand;
import utility.ExitCode;
import utility.Report;

/**
 * Выводит в стандартный поток вывода все элементы коллекции в строковом представлении
 * @author Alina
 */
public class Show extends ExecutableCommand {
    public Show() {
        super("show", "вывести все элементы коллекции в строковом представлении",
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
            CollectionManager collectionManager = CollectionManager.getCollectionManager();
            String message = collectionManager.toString();
            report = new Report(ExitCode.OK.code, null, message);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
