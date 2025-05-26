package main.java.commands;

import entity.Album;
import entity.MusicBand;
import main.java.managers.CollectionManager;
import commands.ExecutableCommand;
import org.apache.commons.lang3.SerializationUtils;
import utility.ExitCode;
import commands.Report;
import utility.Types;

import java.util.*;

/**
 * Удаляет из коллекции один элемент, значение поля bestAlbum которого эквивалентно заданному.
 * @author Alina
 */
public class RemoveAnyByBestAlbum extends ExecutableCommand {
    public RemoveAnyByBestAlbum() {
        super("remove_any_by_best_album",
                "удалить из коллекции один элемент, значение поля bestAlbum которого эквивалентно заданному",
                new Types[]{}, new Types[]{Types.ALBUM});
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
            Collection<Integer> keysToRemove = new ArrayList<>();
            Album targetAlbum;

            // Декодируем и десериализуем объект Album из аргумента
            byte[] data = Base64.getDecoder().decode(args[1]);
            targetAlbum = SerializationUtils.deserialize(data);

            // Находим ключ первого элемента с matching album
            Optional<Map.Entry<Integer, MusicBand>> entryOpt = CollectionManager.getCollection().entrySet().stream()
                    .filter(entry -> targetAlbum.equals(entry.getValue().getBestAlbum()))
                    .findFirst();

            if (entryOpt.isPresent()) {
                Integer key = entryOpt.get().getKey();
                CollectionManager.getCollection().remove(key);
                message = "Удалён элемент с ключом " + key;
            } else {
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
