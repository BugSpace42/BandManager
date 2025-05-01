package commands;

import entity.MusicBand;
import exceptions.CanceledCommandException;
import managers.CollectionManager;
import managers.ConsoleManager;
import utility.ExitCode;

import java.util.HashMap;

/**
 * Заменяет значение по ключу, если новое значение больше старого.
 * @author Alina
 */
public class ClientReplaceIfGreater extends ReplaceIfGreaterCommand {
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
        MusicBand musicBand;
        try {
            musicBand = utility.entityaskers.MusicBandAsker.askMusicBand();
        } catch (CanceledCommandException e) {
            ConsoleManager.println(e.getMessage());
            return ExitCode.CANCEL.code;
        }

        // ссылка на коллекцию
        HashMap<Integer, MusicBand> collection = collectionManager.getCollection();
        
        if (musicBand.compareTo(collection.get(key)) > 0) {
            collectionManager.addToCollection(key, musicBand);
            ConsoleManager.println("Элемент по введённому ключу заменён на заданный.");
        }
        else{
            ConsoleManager.println("Элемент по введённому больше, чем заданный.");
            ConsoleManager.println("Элемент по введённому ключу не изменён.");
        }
        return ExitCode.OK.code;
    }
}
