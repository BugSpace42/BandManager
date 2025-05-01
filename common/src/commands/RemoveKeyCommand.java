package commands;

import utility.Command;

public abstract class RemoveKeyCommand extends Command {
    public RemoveKeyCommand() {
        super("remove_key", "удалить элемент из коллекции по его ключу", 1);
    }
}