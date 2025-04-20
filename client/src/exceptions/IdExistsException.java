package exceptions;

/**
 * Исключение, которое выбрасывается, элемент с введённым id уже существует.
 * @author Alina
 */
public class IdExistsException extends Exception{
    public IdExistsException(String message) {
        super(message);
    }
}
