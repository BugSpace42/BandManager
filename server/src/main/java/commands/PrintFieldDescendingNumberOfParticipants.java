package main.java.commands;

import entity.MusicBand;
import main.java.managers.CollectionManager;
import commands.ExecutableCommand;
import main.java.utility.Commands;
import utility.ExitCode;
import commands.Report;
import utility.Types;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Выводит значения поля numberOfParticipants всех элементов в порядке убывания.
 * @author Alina
 */
public class PrintFieldDescendingNumberOfParticipants extends ExecutableCommand {
    public PrintFieldDescendingNumberOfParticipants() {
        super(Commands.PRINT_DESCENDING_NUMBER,
                "вывести значения поля numberOfParticipants всех элементов в порядке убывания",
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
            String message;
            Collection<MusicBand> collection = CollectionManager.getCollection().values();

            if (collection.isEmpty()) {
                message = "Коллекция пуста.";
            } else {
                String numbersString = collection.stream()
                        .map(MusicBand::getNumberOfParticipants) // получаем число участников
                        .sorted(Comparator.reverseOrder()) // сортируем по убыванию
                        .map(String::valueOf) // преобразуем в строки
                        .collect(Collectors.joining(" ")); // объединяем через пробел

                message = "Значения поля numberOfParticipants всех элементов в порядке убывания:\n" + numbersString + "\n";
            }
            report = new Report(ExitCode.OK.code, null, message);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка: " + e.getMessage();
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
