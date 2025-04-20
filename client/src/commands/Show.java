package commands;

import managers.CollectionManager;
import managers.ConsoleManager;
import utility.Command;
import utility.Runner;

/**
 * Выводит в стандартный поток вывода все элементы коллекции в строковом представлении
 * @author Alina
 */
public class Show extends Command {
    public Show(){
        super("show", "вывести все элементы коллекции в строковом представлении", 0);
    }

    /**
     * Выполняет команду.
     */
    @Override
    public Runner.ExitCode execute(String[] args) {
        CollectionManager collectionManager = CollectionManager.getCollectionManager();
        ConsoleManager.println(collectionManager.toString());
        return Runner.ExitCode.OK;
    }
}
