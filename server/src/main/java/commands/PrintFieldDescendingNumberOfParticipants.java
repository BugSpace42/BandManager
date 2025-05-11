package main.java.commands;

import main.java.utility.ExecutableCommand;
import utility.Report;

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
        // Очищаем коллекцию
        Report report = new Report(0, null, "Коллекция не очищена, команды нету");
        return report;
    }
}
