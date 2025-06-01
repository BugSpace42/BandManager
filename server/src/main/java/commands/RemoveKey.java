package main.java.commands;

import entity.MusicBand;
import exceptions.DatabaseException;
import main.java.managers.CollectionManager;
import commands.ExecutableCommand;
import main.java.managers.DatabaseManager;
import utility.ExitCode;
import commands.Report;
import utility.Types;

import java.util.HashMap;

/**
 * Удаляет элемент из коллекции по его ключу
 * @author Alina
 */
public class RemoveKey extends ExecutableCommand {
    public RemoveKey() {
        super("remove_key", "удалить элемент из коллекции по его ключу",
                new Types[]{Types.MUSIC_BAND_KEY_CONTAINED}, new Types[]{});
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
            Integer key = Integer.valueOf(args[1]);
            HashMap<Integer, MusicBand> collection = CollectionManager.getCollection();

            if (collection.keySet().stream().anyMatch(k -> k.equals(key))) {
                DatabaseManager.removeMusicBandByKey(key);
                collection.remove(key);
                message = "Элемент с ключом " + key + " успешно удалён.";
                report = new Report(ExitCode.OK.code, null, message);
            } else {
                message = "В коллекции нет элемента с ключом " + key;
                report = new Report(ExitCode.ERROR.code, message, message);
            }
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
