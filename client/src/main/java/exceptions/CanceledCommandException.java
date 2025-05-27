package main.java.exceptions;

/**
 * Исключение, которое выбрасывается, если выполнение команды было прервано.
 * @author Alina
 */
public class CanceledCommandException extends Exception{
    public CanceledCommandException(String message) {
        super(message);
    }
}
