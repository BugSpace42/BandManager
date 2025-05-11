package commands;

import utility.Command;

public class ClearCommand extends Command {
    public ClearCommand() {
        super("clear", "очистить коллекцию", 0, new String[]{});
    }
}
