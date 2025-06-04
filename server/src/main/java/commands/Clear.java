package main.java.commands;

import exceptions.WrongUserException;
import main.java.managers.CollectionManager;
import commands.ExecutableCommand;
import main.java.utility.Commands;
import utility.ExitCode;
import commands.Report;
import utility.Types;

import java.util.ArrayList;
import java.util.List;

/**
 * Очищает коллекцию.
 * @author Alina
 */
public class Clear extends ExecutableCommand {
    public Clear() {
        super(Commands.CLEAR, "очистить коллекцию", new Types[]{}, new Types[]{});
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
            List<Integer> keys = CollectionManager.getKeyList();
            String owner = args[1];
            int counter = 0;
            ArrayList<Integer> notRemoved = new ArrayList<>();
            for (Integer key : keys) {
                try {
                    if (! CollectionManager.checkOwner(owner, key)) {
                        notRemoved.add(key);
                        throw new WrongUserException("Невозможно удалить элемент коллекции. " +
                                "Операцию совершает не владелец элемента. Владелец элемента: " + owner);
                    }
                    collectionManager.removeByKey(key);
                    counter++;
                } catch (WrongUserException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Элемент коллекции пропущен.");
                }
            }
            // String message = "Коллекция очищена.";
            String message = "Удалены все элементы, доступные для удаления. Удалено " + counter + " элементов";
            if (notRemoved.size() > 0) {
                message += "\nНе были удалены элементы с ключами: " + notRemoved.toString();
            }
            report = new Report(ExitCode.OK.code, null, message);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
