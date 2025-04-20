package exceptions;

/**
 * Исключение, которое выбрасывается, если введено неверное значение поля.
 * @author Alina
 */
public class WrongValueException extends Exception{
    public WrongValueException(String message) {
        super(message);
    }
}
