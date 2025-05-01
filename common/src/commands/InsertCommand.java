package commands;

import utility.Command;

public abstract class InsertCommand extends Command {
    public InsertCommand() {
        super("insert", "добавить новый элемент с заданным ключом", 1);
    }
}
