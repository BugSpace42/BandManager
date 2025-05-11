package commands;

import utility.Command;

public abstract class ShowCommand extends Command {
    public ShowCommand(){
        super("show", "вывести все элементы коллекции в строковом представлении",
                0, new String[]{});
    }
}
