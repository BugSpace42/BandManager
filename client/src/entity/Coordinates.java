package entity;

import exceptions.CanceledCommandException;
import managers.ConsoleManager;
import utility.builders.CoordinatesBuilder;
import utility.validators.musicband.coordinates.CoordXValidator;
import utility.validators.musicband.coordinates.CoordYValidator;

/**
 * Класс координат.
 * @author Alina
 */
public class Coordinates {
    private final Integer x; //Поле не может быть null
    private final long y; //Значение поля должно быть больше -973

    /**
     * Конструктор - создание нового объекта с заданными x и y.
     * @param x - первая координата
     * @param y - вторая координата
     */
    public Coordinates(Integer x, long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Запрашивает у пользователя объект класса Coordinates.
     * @return введённый объект класса Coordinates
     * @throws CanceledCommandException 
     */
         public static Coordinates askCoordinates() throws CanceledCommandException {
        Integer x = askCoordX();
        long y = askCoordY();
        Coordinates coordinates = CoordinatesBuilder.build(x, y);
        return coordinates;
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

    public Integer getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    /**
     * Проверка равенcтва двух координат.
     * @param coordinates - координаты сравниваемого объекта
     * @return true - если объекты одинаковые, false - если разные
     */
    public boolean equals(Coordinates coordinates) {
        if (coordinates == this) return true;
        return coordinates.x.equals(this.x) && coordinates.y == this.y;
    }
    
    /**
     * @return Строковое представление объекта класса.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}