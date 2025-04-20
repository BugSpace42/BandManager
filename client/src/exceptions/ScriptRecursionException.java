package exceptions;

/**
 * Исключение, которое выбрасывается, если должен выполниться скрипт, который уже выполняется.
 * @author Alina
 */
public class ScriptRecursionException extends Exception{
    public ScriptRecursionException(String message) {
        super(message);
    }
}
