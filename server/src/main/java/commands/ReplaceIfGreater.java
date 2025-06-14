package main.java.commands;

import entity.MusicBand;
import exceptions.DatabaseException;
import exceptions.WrongUserException;
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

/**
 * Заменяет значение по ключу, если новое значение больше старого.
 * @author Alina
 */
public class ReplaceIfGreater extends ExecutableCommand {
    public ReplaceIfGreater() {
        super(Commands.REPLACE_IF_GREATER,
                "заменить значение по ключу, если новое значение больше старого",
                new Types[]{Types.MUSIC_BAND_KEY_CONTAINED}, new Types[]{Types.MUSIC_BAND});
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
            CollectionManager collectionManager = CollectionManager.getCollectionManager();
            Integer key = Integer.valueOf(args[1]);

            byte[] data = Base64.getDecoder().decode(args[2]);
            MusicBand newMusicBand = SerializationUtils.deserialize(data);
            HashMap<Integer, MusicBand> collection = CollectionManager.getCollection();

            if (!collection.keySet().stream().anyMatch(k -> k.equals(key))) {
                String message = "В коллекции нет элемента с ключом " + key;
                report = new Report(ExitCode.ERROR.code, message, message);
            } else {
                String owner = args[3];
                if (! CollectionManager.checkOwner(owner, key)) {
                    throw new WrongUserException("Невозможно изменить элемент коллекции. " +
                            "Операцию совершает не владелец элемента. Владелец элемента: " + owner);
                }
                MusicBand currentMusicBand = collection.get(key);
                String message;
                if (newMusicBand.compareTo(currentMusicBand) > 0) {
                    DatabaseManager.updateMusicBandByKey(key, newMusicBand);
                    collectionManager.updateElementByKey(key, newMusicBand);
                    message = "Элемент с ключом " + key + " заменён на заданный.";
                } else {
                    message = "Элемент с ключом " + key + " больше, чем заданный.\n" +
                              "Элемент с ключом " + key + " не изменён.";
                }
                report = new Report(ExitCode.OK.code, null, message);
            }
        } catch (WrongUserException e){
            String errorString = "Ошибка при замене элемента, связанная с правами доступа.";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        } catch (DatabaseException e){
            String errorString = "Ошибка при замене элемента в базе данных.";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка: " + e.getMessage();
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
