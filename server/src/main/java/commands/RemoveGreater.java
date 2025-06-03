package main.java.commands;

import entity.MusicBand;
import exceptions.DatabaseException;
import main.java.managers.CollectionManager;
import commands.ExecutableCommand;
import main.java.managers.DatabaseManager;
import main.java.utility.Commands;
import org.apache.commons.lang3.SerializationUtils;
import utility.ExitCode;
import commands.Report;
import utility.Types;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Удаляет из коллекции все элементы, превышающие заданный.
 * @author Alina
 */
public class RemoveGreater extends ExecutableCommand {
    public RemoveGreater() {
        super(Commands.REMOVE_GREATER,
                "удалить из коллекции все элементы, превышающие заданный",
                new Types[]{}, new Types[]{Types.MUSIC_BAND});
    }

    /**
     * Выполняет команду.
     * @param args аргументы команды
     * @return отчёт о выполнении команды
     */
    @Override
    public Report execute(String[] args) {
        Report report;
        try {
            String message;
            // Декодируем и десериализуем объект MusicBand из аргумента
            byte[] data = Base64.getDecoder().decode(args[1]);
            MusicBand targetBand = SerializationUtils.deserialize(data);

            // Получаем коллекцию
            HashMap<Integer, MusicBand> collection = CollectionManager.getCollection();

            // Находим все ключи элементов, которые больше заданного
            List<Integer> keysToRemove = collection.entrySet().stream()
                    .filter(entry -> entry.getValue().compareTo(targetBand) > 0)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            if (keysToRemove.isEmpty()) {
                message = "Не найдено элементов, больших заданного.";
            } else {
                for (Integer key : keysToRemove) {
                    DatabaseManager.removeMusicBandByKey(key);
                    CollectionManager.getCollection().remove(key);
                }
                message = "Удалено элементов: " + keysToRemove.size();
            }

            report = new Report(ExitCode.OK.code, null, message);
        } catch (DatabaseException e){
            String errorString = "Ошибка при удалении элемента в базе данных.";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
