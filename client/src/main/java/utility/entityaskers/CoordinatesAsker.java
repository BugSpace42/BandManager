package main.java.utility.entityaskers;

import entity.Coordinates;
import exceptions.CanceledCommandException;
import main.java.managers.ConsoleManager;
import main.java.utility.builders.CoordinatesBuilder;
import main.java.utility.validators.musicband.coordinates.CoordXValidator;
import main.java.utility.validators.musicband.coordinates.CoordYValidator;

/**
 * Отвечает за запрос у пользователя местоположения.
 * @author Alina
 */
public class CoordinatesAsker {
    /**
     * Запрашивает у пользователя объект класса Coordinates.
     * @return введённый объект класса Coordinates
     * @throws CanceledCommandException
     */
    public static Coordinates askCoordinates() throws CanceledCommandException {
        Integer x = askCoordX();
        long y = askCoordY();
        return CoordinatesBuilder.build(x, y);
    }

    /**
     * Запрашивает у пользователя координату x.
     * @return введённая координата.
     * @throws CanceledCommandException
     */
    public static Integer askCoordX() throws CanceledCommandException {
        ConsoleManager.println("Введите координату x.");
        ConsoleManager.println("Координата x должна быть числом типа Integer.");
        String xString = ConsoleManager.askObject();
        Integer x;
        CoordXValidator validator = new CoordXValidator();
        try {
            x = Integer.valueOf(xString);
        } catch (NumberFormatException e) {
            ConsoleManager.println("Введённая строка не является числом типа Integer.");
            ConsoleManager.println("Попробуйте снова.");
            x = askCoordX();
        }
        if (validator.validate(x)) {
            return x;
        }
        else {
            ConsoleManager.println("Введена некорректная координата.");
            ConsoleManager.println("Попробуйте снова.");
            // запрашиваем у пользователя данные, пока не введёт подходящие
            x = askCoordX();
        }
        return x;
    }

    /**
     * Запрашивает у пользователя координату y.
     * @return введённая координата.
     * @throws CanceledCommandException
     */
    public static long askCoordY() throws CanceledCommandException {
        ConsoleManager.println("Введите координату y.");
        ConsoleManager.println("Координата y должна быть числом типа long, большим чем -973.");
        String yString = ConsoleManager.askObject();
        long y;
        CoordYValidator validator = new CoordYValidator();
        try {
            y = Long.parseLong(yString);
        } catch (NumberFormatException e) {
            ConsoleManager.println("Введённая строка не является числом типа long.");
            ConsoleManager.println("Попробуйте снова.");
            y = askCoordY();
        }
        if (validator.validate(y)) {
            return y;
        }
        else {
            ConsoleManager.println("Введена некорректная координата.");
            ConsoleManager.println("Попробуйте снова.");
            // запрашиваем у пользователя данные, пока не введёт подходящие
            y = askCoordY();
        }
        return y;
    }
}
