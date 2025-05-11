package main.java.commands;

import entity.MusicBand;
import main.java.managers.CollectionManager;
import main.java.utility.ExecutableCommand;
import org.apache.commons.lang3.SerializationUtils;
import utility.ExitCode;
import utility.Report;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Удаляет из коллекции все элементы, превышающие заданный.
 * @author Alina
 */
public class RemoveGreater extends ExecutableCommand {
    public RemoveGreater() {
        super("remove_greater",
                "удалить из коллекции все элементы, превышающие заданный",
                0, new String[]{"MusicBand"});
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
            byte[] data = Base64.getDecoder().decode(args[1]);
            MusicBand musicBand = SerializationUtils.deserialize(data);
            CollectionManager collectionManager = CollectionManager.getCollectionManager();
            HashMap<Integer, MusicBand> collection = CollectionManager.getCollection();

            String message = "";
            boolean isRemoved = false;
            for (Map.Entry<Integer, MusicBand> entry : collection.entrySet()) {
                if (entry.getValue().compareTo(musicBand) > 0) {
                    collectionManager.removeByKey(entry.getKey());
                    message = "Удалён элемент с ключом " + entry.getKey();
                    isRemoved = true;
                    break;
                }
            }
            if (!isRemoved) {
                message = "Не найдено элементов, больших заданного.";
            }
            report = new Report(ExitCode.OK.code, null, message);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
