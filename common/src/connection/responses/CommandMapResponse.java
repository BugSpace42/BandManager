package connection.responses;

import java.util.HashMap;

import commands.Command;

public class CommandMapResponse extends Response {
    private final HashMap<String, Command> commands;

    public CommandMapResponse(HashMap<String, Command> commands) {
        this.commands = commands;
    }

    public HashMap<String, Command> getCommandMap() {
        return commands;
    }
}
