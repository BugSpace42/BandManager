package commands;

import utility.Command;

public abstract class PrintFieldDescendingNumberOfParticipantsCommand extends Command {
    public PrintFieldDescendingNumberOfParticipantsCommand() {
        super("print_field_descending_number_of_participants",
                "вывести значения поля numberOfParticipants всех элементов в порядке убывания", 0);
    }
}
