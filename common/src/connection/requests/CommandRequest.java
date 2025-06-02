package connection.requests;

public class CommandRequest extends UserRequest {
    private final String name;
    private final String[] args;

    public CommandRequest(String name, String[] args, String username, String password) {
        super(username, password);
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public String[] getArgs() {
        return args;
    }
}
