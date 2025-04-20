package utility.builders;

import entity.Coordinates;

/**
 * Управляет сборкой объекта класса Coordinates 
 * @author Alina
 */
public class CoordinatesBuilder {
    /**
     * Собирает объект класса Coordinates
     * @param x - первая координата
     * @param y - вторая координата
     * @return бъект класса Coordinates
     */
    public static Coordinates build(Integer x, long y) {
        Coordinates coordinates = new Coordinates(x, y);
        return coordinates;
    }
}
