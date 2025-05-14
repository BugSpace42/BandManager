package connection.responses;

import java.util.HashMap;

import utility.Command;

public class ListOfCommandsResponse extends Response {
    private final HashMap<String, Command> commands;

    public ListOfCommandsResponse(HashMap<String, Command> commands) {
        this.commands = commands;
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }
}
