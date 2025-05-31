package connection.responses;

public class AuthenticationResponse extends Response {
    private final boolean authenticated;
    private final String error;

    public AuthenticationResponse(boolean authenticated, String error) {
        this.authenticated = authenticated;
        this.error = error;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getError() {
        return error;
    }
}
