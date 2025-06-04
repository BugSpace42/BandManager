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

/**
 * Добавляет в коллекцию новый элемент с заданным ключом.
 * @author Alina
 */
public class Insert extends ExecutableCommand {
    public Insert() {
        super(Commands.INSERT, "добавить новый элемент с заданным ключом",
                new Types[]{Types.MUSIC_BAND_KEY_UNIQUE}, new Types[]{Types.MUSIC_BAND});
    }

    /**
     * Выполняет команду.
     * @param args аргументы команды
     * @return отчёт о выполнении команды
     */
    public Report execute(String[] args){
        Report report;
        try {
            byte[] data = Base64.getDecoder().decode(args[2]);
            MusicBand musicBand = SerializationUtils.deserialize(data);

            CollectionManager collectionManager = CollectionManager.getCollectionManager();
            Integer key = Integer.valueOf(args[1]);

            if (collectionManager.containsKey(key)) {
                String errorString = "Элемент с ключом " + key + " уже находится в коллекции.";
                report = new Report(ExitCode.ERROR.code, errorString, errorString);
            }
            else {
                DatabaseManager.addMusicBand(key, musicBand);
                collectionManager.addToCollection(key, musicBand);
                collectionManager.addOwnerToCollection(key, args[3]);
                String message = "Элемент с ключом " + key + " успешно добавлен в коллекцию.";
                report = new Report(ExitCode.OK.code, null, message);
            }
        } catch (DatabaseException e) {
            String errorString = "Ошибка при добавлении элемента в базу данных.";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
