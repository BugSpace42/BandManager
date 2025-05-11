package main.java.commands;

import entity.MusicBand;
import main.java.managers.CollectionManager;
import main.java.utility.ExecutableCommand;
import org.apache.commons.lang3.SerializationUtils;
import utility.ExitCode;
import utility.Report;

import java.util.Base64;

/**
 * Обновляет значение элемента коллекции, id которого равен заданному.
 * @author Alina
 */
public class Update extends ExecutableCommand {
    public Update() {
        super("update",
                "обновить значение элемента коллекции, id которого равен заданному",
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
                collectionManager.updateElementById(id, musicBand);
                message = "Элемент по id " + id + " успешно изменён на заданный.";
                report = new Report(ExitCode.OK.code, null, message);
            }
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
