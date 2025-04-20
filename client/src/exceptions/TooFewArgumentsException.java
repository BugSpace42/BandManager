package exceptions;

/**
 * Исключение, которое выбрасывается, если введено слишком мало аргументов. 
 * @author Alina
 */
public class TooFewArgumentsException extends Exception{
    public TooFewArgumentsException(String message) {
        super(message);
    }
}
