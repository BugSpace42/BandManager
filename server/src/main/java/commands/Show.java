package main.java.commands;

import entity.MusicBand;
import main.java.managers.CollectionManager;
import utility.ExecutableCommand;
import utility.ExitCode;
import utility.Report;

import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Выводит в стандартный поток вывода все элементы коллекции в строковом представлении.
 * Элементы отсортированы по возрастанию имени.
 * @author Alina
 */
public class Show extends ExecutableCommand {
    public Show() {
        super("show", "вывести все элементы коллекции в строковом представлении",
                0, new String[]{});
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
            HashMap<Integer, MusicBand> collection = CollectionManager.getCollection();

            String message = collection.values().stream()
                    .sorted(Comparator.comparing(MusicBand::getName))
                    .map(MusicBand::toString)
                    .collect(Collectors.joining("\n"));

            report = new Report(ExitCode.OK.code, null, message);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
