package commands;

import utility.Command;

public abstract class SaveCommand extends Command {
    public SaveCommand() {
        super("save", "сохранить коллекцию в файл",
                0, new String[]{});
    }
}
