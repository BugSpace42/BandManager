package main.java.utility;

public abstract class ExecutableCommand extends Command implements Executable {
    public ExecutableCommand(String name, String description, int numberOfArguments, String[] arguments) {
        super(name, description, numberOfArguments, arguments);
    }
}
