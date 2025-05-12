package main.java.connection.requests;

public class CommandRequest extends Request{
    private String name;
    private String[] args;

    public CommandRequest(String name, String[] args) {
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
