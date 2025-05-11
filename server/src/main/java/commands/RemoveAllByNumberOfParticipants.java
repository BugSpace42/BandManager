package main.java.commands;

import entity.MusicBand;
import main.java.utility.ExecutableCommand;
import utility.ExitCode;
import utility.Report;
import main.java.managers.CollectionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Удаляет из коллекции все элементы, значение поля numberOfParticipants которого эквивалентно заданному.
 * @author Alina
 */
public class RemoveAllByNumberOfParticipants extends ExecutableCommand {
    public RemoveAllByNumberOfParticipants() {
        super("remove_all_by_number_of_participants",
                "удалить из коллекции все элементы, значение поля numberOfParticipants которого эквивалентно заданному",
                1, new String[]{});
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
            String message = "";

            HashMap<Integer, MusicBand> collection = CollectionManager.getCollection();

            boolean isRemoved = false;
            for (Map.Entry<Integer, MusicBand> entry : collection.entrySet()) {
                if (entry.getValue().getNumberOfParticipants().equals(Integer.valueOf(args[1]))) {
                    collectionManager.removeByKey(entry.getKey());
                    message = "Удалён элемент с ключом " + entry.getKey();
                    isRemoved = true;
                }
            }
            if (!isRemoved) {
                message = "Не найдено элементов с заданным полем bestAlbum.";
            }
            report = new Report(ExitCode.OK.code, null, message);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
