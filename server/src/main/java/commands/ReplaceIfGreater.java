package main.java.commands;

import entity.MusicBand;
import main.java.managers.CollectionManager;
import main.java.utility.ExecutableCommand;
import org.apache.commons.lang3.SerializationUtils;
import utility.ExitCode;
import utility.Report;

import java.util.Base64;
import java.util.HashMap;

/**
 * Заменяет значение по ключу, если новое значение больше старого.
 * @author Alina
 */
public class ReplaceIfGreater extends ExecutableCommand {
    public ReplaceIfGreater() {
        super("replace_if_greater",
                "заменить значение по ключу, если новое значение больше старого",
                1, new String[]{"MusicBand"});
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
            Integer key = Integer.valueOf(args[1]);
            String message = "";

            byte[] data = Base64.getDecoder().decode(args[2]);
            MusicBand musicBand = SerializationUtils.deserialize(data);
            HashMap<Integer, MusicBand> collection = CollectionManager.getCollection();

            if (!collection.containsKey(key)) {
                message = "В коллекции нет элемента с ключом " + key;
                report = new Report(ExitCode.ERROR.code, message, message);
            }
            else {
                if (musicBand.compareTo(collection.get(key)) > 0) {
                    collectionManager.addToCollection(key, musicBand);
                    message = "Элемент с ключом " + key + " заменён на заданный.";
                }
                else{
                    message = "Элемент с ключом " + key + " больше, чем заданный.\n";
                    message += "Элемент с ключом " + key + " не изменён.";
                }
                report = new Report(ExitCode.OK.code, null, message);
            }
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
