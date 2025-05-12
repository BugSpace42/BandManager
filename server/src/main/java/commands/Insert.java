package main.java.commands;

import entity.MusicBand;
import main.java.managers.CollectionManager;
import main.java.utility.ExecutableCommand;
import org.apache.commons.lang3.SerializationUtils;
import main.java.utility.ExitCode;
import main.java.utility.Report;

import java.util.Base64;

/**
 * Добавляет в коллекцию новый элемент с заданным ключом.
 * @author Alina
 */
public class Insert extends ExecutableCommand {
    public Insert() {
        super("insert", "добавить новый элемент с заданным ключом",
                1, new String[]{"MusicBand"});
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
            collectionManager.addToCollection(Integer.valueOf(args[1]), musicBand);
            String message = "Элемент с ключом " + args[1] + " успешно добавлен в коллекцию.";
            report = new Report(ExitCode.OK.code, null, message);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
