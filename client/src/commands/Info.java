package commands;

import entity.MusicBand;
import managers.ConsoleManager;
import utility.Command;
import utility.Runner;
import utility.Runner.ExitCode;

import java.util.HashMap;

/**
 * Выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
 * @author Alina
 */
public class Info extends Command{
    public Info() {
        super("info", "вывести информацию о коллекции", 0);
    }

    /**
     * Выполняет команду.
     */
    // todo
    @Override
    public ExitCode execute(String[] args) {
        Runner runner = Runner.getRunner();
        try {
            HashMap<Integer, MusicBand> collection = runner.collectionManager.getCollection();
            ConsoleManager.println("Информация о коллекции:");
            ConsoleManager.println(" Тип коллекции: " + collection.getClass().getName());
            ConsoleManager.println(" Дата инициализации коллекции: " + runner.collectionManager.getInitDate());
            ConsoleManager.println(" Количество элементов: " + collection.size());
            return ExitCode.OK;
        } catch (Exception e) {
            ConsoleManager.printError("Непредвиденная ошибка!");
            return ExitCode.ERROR;
        }
    }
}
