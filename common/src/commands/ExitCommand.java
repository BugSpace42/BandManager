package commands;

import utility.Command;

public abstract class ExitCommand extends Command {
    public ExitCommand() {
        super("exit", "завершить программу (без сохранения в файл)", 0);
    }
}