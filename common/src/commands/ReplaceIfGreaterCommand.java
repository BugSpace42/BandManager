package commands;

import utility.Command;

public abstract class ReplaceIfGreaterCommand extends Command {
    public ReplaceIfGreaterCommand() {
        super("replace_if_greater",
                "заменить значение по ключу, если новое значение больше старого", 1);
    }
}
