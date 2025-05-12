package main.java.commands;

import entity.MusicBand;
import main.java.managers.CollectionManager;
import main.java.utility.ExecutableCommand;
import main.java.utility.ExitCode;
import main.java.utility.Report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Выводит значения поля numberOfParticipants всех элементов в порядке убывания.
 * @author Alina
 */
public class PrintFieldDescendingNumberOfParticipants extends ExecutableCommand {
    public PrintFieldDescendingNumberOfParticipants() {
        super("print_field_descending_number_of_participants",
                "вывести значения поля numberOfParticipants всех элементов в порядке убывания",
                0, new String[]{});
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
            if (CollectionManager.getCollection().isEmpty()) {
                message = "Коллекция пуста.";
            }
            else {
                ArrayList<Integer> numberOfParticipantsList = new ArrayList<>();

                for (Map.Entry<Integer, MusicBand> entry : CollectionManager.getCollection().entrySet()) {
                    numberOfParticipantsList.add(entry.getValue().getNumberOfParticipants());
                }

                numberOfParticipantsList.sort(Collections.reverseOrder());
                message = "Значения поля numberOfParticipants всех элементов в порядке убывания:\n";
                for (Integer number : numberOfParticipantsList) {
                    message += number + " ";
                }
                message += "\n";
            }
            report = new Report(ExitCode.OK.code, null, message);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
