package utility;

public abstract class ExecutableCommand extends Command implements Executable {
    public ExecutableCommand(String name, String description, Types[] positionalArguments, Types[] arguments) {
        super(name, description, positionalArguments, arguments);
    }
}
