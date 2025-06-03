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

/**
 * Обновляет значение элемента коллекции, id которого равен заданному.
 * @author Alina
 */
public class Update extends ExecutableCommand {
    public Update() {
        super(Commands.UPDATE,
                "обновить значение элемента коллекции, id которого равен заданному",
                new Types[]{Types.MUSIC_BAND_ID_CONTAINED}, new Types[]{Types.MUSIC_BAND});
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
            String message;
            byte[] data = Base64.getDecoder().decode(args[2]);
            MusicBand musicBand = SerializationUtils.deserialize(data);

            CollectionManager collectionManager = CollectionManager.getCollectionManager();
            Long id = Long.valueOf(args[1]);

            if (collectionManager.getById(id) == null) {
                message = "В коллекции нет элемента с id " + id;
                report = new Report(ExitCode.ERROR.code, message, message);
            }
            else {
                DatabaseManager.updateMusicBandById(id, musicBand);
                collectionManager.updateElementById(id, musicBand); // TODO (говорит, что не заменяется элемент, но он заменяется)
                message = "Элемент по id " + id + " успешно изменён на заданный.";
                report = new Report(ExitCode.OK.code, null, message);
            }
        } catch (DatabaseException e){
            String errorString = "Ошибка при обновлении элемента в базе данных.";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
