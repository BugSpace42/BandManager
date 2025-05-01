package commands;

import utility.Command;

public abstract class RemoveGreaterCommand extends Command {
    public RemoveGreaterCommand() {
        super("remove_greater",
                "удалить из коллекции все элементы, превышающие заданный", 0);
    }
}
