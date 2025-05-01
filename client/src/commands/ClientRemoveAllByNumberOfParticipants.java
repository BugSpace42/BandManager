package commands;

import entity.MusicBand;
import managers.CollectionManager;
import managers.ConsoleManager;
import utility.ExitCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Удаляет из коллекции все элементы, значение поля numberOfParticipants которого эквивалентно заданному.
 * @author Alina
 */
public class ClientRemoveAllByNumberOfParticipants extends RemoveAllByNumberOfParticipantsCommand {
    /**
     * Выполняет команду.
     */
    @Override
    public int execute(String[] args){
        CollectionManager collectionManager = CollectionManager.getCollectionManager();
        try {
            Integer numberOfParticipants = utility.entityaskers.MusicBandAsker.askMusicBandNumber();

            // ссылка на коллекцию, которую будем изменять
            HashMap<Integer, MusicBand> collection = collectionManager.getCollection();
            
            boolean isRemoved = false;
            for (Map.Entry<Integer, MusicBand> entry : collection.entrySet()) {
                if (entry.getValue().getNumberOfParticipants().equals(numberOfParticipants)) {
                    collectionManager.removeByKey(entry.getKey());
                    ConsoleManager.println("Удалён элемент с ключом " + entry.getKey());
                    isRemoved = true;
                }
            }
            if (!isRemoved) {
                ConsoleManager.println("Не найдено элементов с заданным полем bestAlbum.");
            }
            return ExitCode.OK.code;
        } catch (Exception e) {
            ConsoleManager.printError("Непредвиденная ошибка!");
            return ExitCode.ERROR.code;
        }
    }

}
