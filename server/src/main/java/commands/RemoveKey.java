package main.java.commands;

import main.java.managers.CollectionManager;
import main.java.utility.ExecutableCommand;
import utility.ExitCode;
import utility.Report;

/**
 * Удаляет элемент из коллекции по его ключу
 * @author Alina
 */
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
        Report report;
        try {
            CollectionManager collectionManager = CollectionManager.getCollectionManager();
            String message = "";
            Integer key = Integer.valueOf(args[1]);
            if (!CollectionManager.getCollection().containsKey(key)) {
                message = "В коллекции нет элемента с ключом " + args[1];
                report = new Report(ExitCode.ERROR.code, message, message);
            }
            else {
                collectionManager.removeByKey(key);
                report = new Report(ExitCode.OK.code, null, message);
            }
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
