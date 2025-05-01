package commands;

import managers.CollectionManager;
import managers.ConsoleManager;
import utility.ExitCode;

/**
 * Удаляет элемент из коллекции по его ключу
 * @author Alina
 */
public class ClientRemoveKey extends RemoveKeyCommand {
    /**
     * Выполняет команду.
     */
    @Override
    public int execute(String[] args){
        CollectionManager collectionManager = CollectionManager.getCollectionManager();
        Integer key;
        try {
            key = Integer.valueOf(args[1]);
        } catch (NumberFormatException e) {
            ConsoleManager.printError("Введённый ключ не является числом типа Integer.");
            return ExitCode.ERROR.code;
        }
        if (!collectionManager.getCollection().containsKey(key)) {
            ConsoleManager.printError("В коллекции нет элемента с ключом " + key);
            return ExitCode.ERROR.code;
        }
        collectionManager.removeByKey(key);
        return ExitCode.OK.code;
    }
}
