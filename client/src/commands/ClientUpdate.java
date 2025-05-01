package commands;

import entity.MusicBand;
import exceptions.CanceledCommandException;
import managers.CollectionManager;
import managers.ConsoleManager;
import utility.ExitCode;

/**
 *
 * @author Alina
 */
public class ClientUpdate extends UpdateCommand {
    /**
     * Выполняет команду.
     */
    @Override
    public int execute(String[] args) {
        CollectionManager collectionManager = CollectionManager.getCollectionManager();
        Long id;
        try {
            id = Long.valueOf(args[1]);
        } catch (NumberFormatException e) {
            ConsoleManager.printError("Введённый id не является числом типа Long.");
            return ExitCode.ERROR.code;
        }
        if (collectionManager.getById(id) == null) {
            ConsoleManager.printError("В коллекции нет элемента с id " + id);
            return ExitCode.ERROR.code;
        }
        MusicBand element;
        try {
            element = utility.entityaskers.MusicBandAsker.askMusicBand();
            collectionManager.updateElementById(id, element);
            return ExitCode.OK.code;
        } catch (CanceledCommandException e) {
            ConsoleManager.println(e.getMessage());
            return ExitCode.CANCEL.code;
        }
    }
}
