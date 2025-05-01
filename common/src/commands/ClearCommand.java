package commands;

import utility.Command;

public abstract class ClearCommand extends Command {
    public ClearCommand() {
        super("clear", "очистить коллекцию", 0);
    }
}
