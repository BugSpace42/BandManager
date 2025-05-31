package connection.requests;

import utility.AuthenticationCommands;

public class AuthenticationRequest extends Request {
    private AuthenticationCommands command;
    private String username;
    private String password;

    public AuthenticationRequest(AuthenticationCommands command, String username, String password) {
        this.command = command;
        this.username = username;
        this.password = password;
    }

    public AuthenticationCommands getCommand() {
        return command;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
