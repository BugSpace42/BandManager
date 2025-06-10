package main.java.commands;

import entity.MusicBand;
import main.java.managers.CollectionManager;
import commands.ExecutableCommand;
import main.java.utility.Commands;
import utility.ExitCode;
import commands.Report;
import utility.Types;

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
        super(Commands.SHOW, "вывести все элементы коллекции в строковом представлении",
                new Types[]{}, new Types[]{});
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

            String message = collection.entrySet().stream()
                    .sorted(Comparator.comparing(e -> e.getValue().getName()))
                    .map(entry -> {
                        Integer key = entry.getKey(); // ключ коллекции
                        MusicBand musicBand = entry.getValue(); // значение
                        String bandStr = musicBand.toString();
                        return "Элемент коллекции с ключом " + key + ":\n" + bandStr + "\n";
                    })
                    .collect(Collectors.joining("\n"));

            report = new Report(ExitCode.OK.code, null, message);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка: " + e.getMessage();
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
