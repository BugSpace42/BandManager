package commands;

import entity.MusicBand;
import exceptions.CanceledCommandException;
import managers.ConsoleManager;
import utility.ExitCode;
import utility.Runner;

/**
 * Добавляет в коллекцию новый элемент с заданным ключом.
 * @author Alina
 */
public class ClientInsert extends InsertCommand {
    /**
     * Выполняет команду.
     */
    @Override
    public int execute(String[] args){
        Runner runner = Runner.getRunner();
        Integer key;
        try {
            key = Integer.valueOf(args[1]);
        } catch (NumberFormatException e) {
            ConsoleManager.printError("Введённый ключ не является числом типа Integer.");
            return ExitCode.ERROR.code;
        }
        if (runner.collectionManager.getCollection().containsKey(key)) {
            ConsoleManager.printError("В коллекции уже есть элемент с ключом " + key);
            return ExitCode.ERROR.code;
        }
        
        MusicBand element;
        try {
            element = utility.entityaskers.MusicBandAsker.askMusicBand();
            runner.collectionManager.addToCollection(key, element);
            return ExitCode.OK.code;
        } catch (CanceledCommandException e) {
            ConsoleManager.println(e.getMessage());
            return ExitCode.CANCEL.code;
        }
    }
}
