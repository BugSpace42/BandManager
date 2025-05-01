package utility;

/**
 * Абстрактный класс команды.
 * @author Alina
 */
public abstract class Command implements Executable {
    private final String name;
    private final String description;
    private final int numberOfArguments;

    public Command(String name, String description, int numberOfArguments) {
        this.name = name;
        this.description = description;
        this.numberOfArguments = numberOfArguments;
    }

    public abstract int execute(String[] args);

    /**
     * Возвращает название команды
     * @return название команды
     */
    public String getName() {
        return name;
    }

    /**
     * Вощвращает описание команды.
     * @return описание команды
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает количество агрументов команды.
     * @return количество агрументов команды
     */
    public int getNumberOfArguments() {
        return numberOfArguments;
    }
}

