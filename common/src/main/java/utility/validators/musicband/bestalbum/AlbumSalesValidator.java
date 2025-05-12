package main.java.utility.validators.musicband.bestalbum;

/**
 * Проверка корректности продаж альбома.
 * @author Alina
 */
public class AlbumSalesValidator {
    public boolean validate(Double sales) {
        if (sales == null) {
            // поле не задано
            return false;
        }
        if (sales <= 0) {
            // значение поля должно быть больше 0
            return false;
        }
        return true;
    }
}
