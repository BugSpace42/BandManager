package exceptions;

/**
 * Исключение, которое выбрасывается, если введена неизвестная команда.
 * @author Alina
 */
public class UnknownCommandException extends Exception{
    public UnknownCommandException(String message) {
        super(message);
    }
}
