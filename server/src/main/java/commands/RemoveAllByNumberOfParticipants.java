package main.java.commands;

import commands.ExecutableCommand;
import entity.MusicBand;
import exceptions.DatabaseException;
import exceptions.WrongUserException;
import main.java.managers.DatabaseManager;
import main.java.utility.Commands;
import utility.ExitCode;
import commands.Report;
import main.java.managers.CollectionManager;
import utility.Types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Удаляет из коллекции все элементы, значение поля numberOfParticipants которого эквивалентно заданному.
 * @author Alina
 */
public class RemoveAllByNumberOfParticipants extends ExecutableCommand {
    public RemoveAllByNumberOfParticipants() {
        super(Commands.REMOVE_BY_NUMBER,
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
            CollectionManager collectionManager = CollectionManager.getCollectionManager();
            HashMap<Integer, MusicBand> collection = CollectionManager.getCollection();
            String owner = args[2];
            int counter = 0;
            ArrayList<Integer> notRemoved = new ArrayList<>();
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
                    try {
                        if (! CollectionManager.checkOwner(owner, key)) {
                            notRemoved.add(key);
                            throw new WrongUserException("Невозможно удалить элемент коллекции. " +
                                    "Операцию совершает не владелец элемента. Владелец элемента: " + owner);
                        }
                        DatabaseManager.removeMusicBandByKey(key);
                        collectionManager.removeByKey(key);
                        counter++;
                    } catch (WrongUserException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Элемент коллекции пропущен.");
                    }
                }
                message = "Удалено элементов: " + counter;
                if (notRemoved.size() > 0) {
                    message += "\nНе были удалены элементы с ключами: " + notRemoved.toString();
                }
            }

            report = new Report(ExitCode.OK.code, null, message);
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
