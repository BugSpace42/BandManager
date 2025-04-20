package utility.validators.stringvalidators.musicband.bestalbum;

/**
 * Проверка корректности продаж альбома, лежащих в строке.
 * @author Alina
 */
public class AlbumSalesValidator {
    public boolean validate(String salesString) {
        try {
            Double sales = Double.valueOf(salesString);
            if (sales <= 0) {
                // значение поля должно быть больше 0
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
