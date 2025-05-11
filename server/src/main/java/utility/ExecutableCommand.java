package main.java.utility;

import utility.Command;
import utility.Executable;

public abstract class ExecutableCommand extends Command implements Executable {
    public ExecutableCommand(String name, String description, int numberOfArguments, String[] arguments) {
        super(name, description, numberOfArguments, arguments);
    }
}
