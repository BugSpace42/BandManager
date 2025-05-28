package main.java.exceptions;

/**
 * Исключение, которое выбрасывается, если сервер недоступен.
 * @author Alina
 */
public class ServerIsNotAvailableException extends Exception {
    public ServerIsNotAvailableException(String message) {
        super(message);
    }
}
