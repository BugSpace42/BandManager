package main.java.utility;

import java.io.Serializable;

/**
 * Абстрактный класс команды.
 * @author Alina
 */
public class Command implements Serializable {
    private final String name;
    private final String description;
    private final int numberOfArguments;
    private final String[] arguments;

    public Command(String name, String description, int numberOfArguments, String[] arguments) {
        this.name = name;
        this.description = description;
        this.numberOfArguments = numberOfArguments;
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

    /**
     * Возвращает список типов аргументов команды.
     * @return список типов аргументов команды.
     */
    public String[] getArguments() {
        return arguments;
    }
}

