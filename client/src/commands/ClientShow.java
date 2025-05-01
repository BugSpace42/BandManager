package commands;

import managers.CollectionManager;
import managers.ConsoleManager;
import utility.ExitCode;

/**
 * Выводит в стандартный поток вывода все элементы коллекции в строковом представлении
 * @author Alina
 */
public class ClientShow extends ShowCommand {
    /**
     * Выполняет команду.
     */
    public int execute(String[] args) {
        CollectionManager collectionManager = CollectionManager.getCollectionManager();
        ConsoleManager.println(collectionManager.toString());
        return ExitCode.OK.code;
    }
}
