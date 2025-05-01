package commands;

import utility.Command;

public abstract class UpdateCommand extends Command {
    public UpdateCommand() {
        super("update",
                "обновить значение элемента коллекции, id которого равен заданному", 1);
    }
}
