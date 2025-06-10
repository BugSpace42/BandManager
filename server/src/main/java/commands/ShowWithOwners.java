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
import java.util.List;
import java.util.stream.Collectors;

/**
 * Выводит в стандартный поток вывода все элементы коллекции в строковом представлении.
 * Элементы отсортированы по возрастанию имени.
 * @author Alina
 */
public class ShowWithOwners extends ExecutableCommand {
    public ShowWithOwners() {
        super(Commands.SHOW_WITH_OWNERS, "вывести все элементы коллекции в строковом представлении с их владельцами",
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
                        HashMap<Integer, String> owners = CollectionManager.getMusicBandOwners();
                        String owner = owners.get(key);
                        return "Элемент коллекции с ключом " + key + ":\n" + bandStr + "\n Владелец объекта: " + owner;
                    })
                    .collect(Collectors.joining("\n"));

            report = new Report(ExitCode.OK.code, null, message);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
