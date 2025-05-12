package main.java.commands;

import entity.Album;
import entity.MusicBand;
import main.java.managers.CollectionManager;
import main.java.utility.ExecutableCommand;
import org.apache.commons.lang3.SerializationUtils;
import main.java.utility.ExitCode;
import main.java.utility.Report;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Удаляет из коллекции один элемент, значение поля bestAlbum которого эквивалентно заданному.
 * @author Alina
 */
public class RemoveAnyByBestAlbum extends ExecutableCommand {
    public RemoveAnyByBestAlbum() {
        super("remove_any_by_best_album",
                "удалить из коллекции один элемент, значение поля bestAlbum которого эквивалентно заданному",
                0, new String[]{"Album"});
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
            HashMap<Integer, MusicBand> collection = collectionManager.getCollection();

            byte[] data = Base64.getDecoder().decode(args[1]);
            Album album = SerializationUtils.deserialize(data);

            String message = "";
            boolean isRemoved = false;
            for (Map.Entry<Integer, MusicBand> entry : collection.entrySet()) {
                if (album.equals(entry.getValue().getBestAlbum())) {
                    collectionManager.removeByKey(entry.getKey());
                    message = "Удалён элемент с ключом " + entry.getKey();
                    isRemoved = true;
                    break;
                }
            }
            if (!isRemoved) {
                message = "Не найдено элементов с заданным полем bestAlbum.";
            }
            report = new Report(ExitCode.OK.code, null, message);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            System.out.println(e.getMessage());
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
