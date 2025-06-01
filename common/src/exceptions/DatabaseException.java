package exceptions;

/**
 * Исключение, которое выбрасывается при проблемах в работе с базой данных.
 * @author Alina
 */
public class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
    }
}
