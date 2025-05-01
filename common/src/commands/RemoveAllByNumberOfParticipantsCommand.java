package commands;

import utility.Command;

public abstract class RemoveAllByNumberOfParticipantsCommand extends Command {
    public RemoveAllByNumberOfParticipantsCommand() {
        super("remove_all_by_number_of_participants",
                "удалить из коллекции все элементы, значение поля numberOfParticipants которого эквивалентно заданному", 1);
    }
}
