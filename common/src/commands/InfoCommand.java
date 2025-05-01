package commands;

import utility.Command;


public abstract class InfoCommand extends Command {
    public InfoCommand() {
        super("info", "вывести информацию о коллекции", 0);
    }
}
