package commands;

import utility.Command;

public abstract class HelpCommand extends Command {
    public HelpCommand(){
        super("help", "вывести справку по доступным командам", 0, new String[]{});
    }
}