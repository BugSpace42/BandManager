package main.java.commands;

import main.java.utility.ExecutableCommand;
import utility.Report;

public class RemoveAllByNumberOfParticipants extends ExecutableCommand {
    public RemoveAllByNumberOfParticipants() {
        super("remove_all_by_number_of_participants",
                "удалить из коллекции все элементы, значение поля numberOfParticipants которого эквивалентно заданному",
                1, new String[]{});
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
