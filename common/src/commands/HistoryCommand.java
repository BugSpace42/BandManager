package commands;

import utility.Command;

public abstract class HistoryCommand extends Command {
    public HistoryCommand() {
        super("history", "вывести последние 8 команд", 0, new String[]{});
    }
}