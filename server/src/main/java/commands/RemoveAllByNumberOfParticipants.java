package main.java.commands;

import utility.ExecutableCommand;
import utility.ExitCode;
import utility.Report;
import main.java.managers.CollectionManager;
import utility.Types;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Удаляет из коллекции все элементы, значение поля numberOfParticipants которого эквивалентно заданному.
 * @author Alina
 */
public class RemoveAllByNumberOfParticipants extends ExecutableCommand {
    public RemoveAllByNumberOfParticipants() {
        super("remove_all_by_number_of_participants",
                "удалить из коллекции все элементы, значение поля numberOfParticipants которого эквивалентно заданному",
                new Types[]{Types.MUSIC_BAND_NUMBER}, new Types[]{});
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
            int targetNumber;
            try {
                targetNumber = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return new Report(ExitCode.ERROR.code, "Некорректный формат числа.", "Ошибка при парсинге аргумента.");
            }

            Collection<Integer> keysToRemove = CollectionManager.getCollection().entrySet().stream()
                    .filter(entry -> entry.getValue().getNumberOfParticipants().equals(targetNumber))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            if (keysToRemove.isEmpty()) {
                message = "Не найдено элементов с заданным полем numberOfParticipants.";
            } else {
                for (Integer key : keysToRemove) {
                    CollectionManager.getCollection().remove(key);
                }
                message = "Удалено элементов: " + keysToRemove.size();
            }

            report = new Report(ExitCode.OK.code, null, message);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
