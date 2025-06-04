package exceptions;

public class WrongUserException extends Exception {
    public WrongUserException(String message) {
        super(message);
    }
}
