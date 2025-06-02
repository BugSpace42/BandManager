package connection.requests;

import utility.AuthenticationCommands;

public class AuthenticationRequest extends UserRequest {
    private final AuthenticationCommands command;

    public AuthenticationRequest(AuthenticationCommands command, String username, String password) {
        super(username, password);
        this.command = command;
    }

    public AuthenticationCommands getCommand() {
        return command;
    }
}
