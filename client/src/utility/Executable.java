package utility;

/**
 * Интерфейс исполняемых объектов.
 * @author Alina
 */
public interface Executable {
    Runner.ExitCode execute(String[] args);
}
