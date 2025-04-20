package commands;

import entity.MusicBand;
import managers.CollectionManager;
import managers.ConsoleManager;
import utility.Command;
import utility.Runner.ExitCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Удаляет из коллекции все элементы, значение поля numberOfParticipants которого эквивалентно заданному.
 * @author Alina
 */
public class RemoveAllByNumberOfParticipants extends Command{
    public RemoveAllByNumberOfParticipants() {
        super("remove_all_by_number_of_participants", 
              "удалить из коллекции все элементы, значение поля numberOfParticipants которого эквивалентно заданному", 1);
    }
    
    /**
     * Выполняет команду.
     */
    @Override
    public ExitCode execute(String[] args){
        CollectionManager collectionManager = CollectionManager.getCollectionManager();
        try {
            Integer numberOfParticipants = MusicBand.askMusicBandNumber();

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
            return ExitCode.OK;
        } catch (Exception e) {
            ConsoleManager.printError("Непредвиденная ошибка!");
            return ExitCode.ERROR;
        }
    }

}
