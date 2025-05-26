package utility;

import java.io.Serializable;

/**
 * Абстрактный класс команды.
 * @author Alina
 */
public class Command implements Serializable {
    private final String name;
    private final String description;
    private final Types[] positionalArguments;
    private final Types[] arguments;

    public Command(String name, String description, Types[] positionalArguments, Types[] arguments) {
        this.name = name;
        this.description = description;
        this.positionalArguments = positionalArguments;
        this.arguments = arguments;
    }

    /**
     * Возвращает название команды
     * @return название команды
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает описание команды.
     * @return описание команды
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает количество позиционных аргументов команды.
     * @return количество позиционных аргументов команды
     */
    public int getNumberOfPositionalArguments() {
        return positionalArguments.length;
    }

    /**
     * Возвращает количество аргументов команды.
     * @return количество аргументов команды
     */
    public int getNumberOfArguments() {
        return arguments.length;
    }

    /**
     * Возвращает список типов позиционных аргументов команды.
     * @return список типов позиционных аргументов команды.
     */
    public Types[] getPositionalArguments() {
        return positionalArguments;
    }

    /**
     * Возвращает список типов аргументов команды.
     * @return список типов аргументов команды.
     */
    public Types[] getArguments() {
        return arguments;
    }
}

