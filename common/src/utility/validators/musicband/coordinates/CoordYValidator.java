package utility.validators.musicband.coordinates;

import utility.validators.Validator;

/**
 * Проверка корректности координаты y.
 * @author Alina
 */
public class CoordYValidator implements Validator<Long>{
    @Override
    public boolean validate(Long y) {
        if (y == null) {
            // координата не задана
            return false;
        }
        if (y <= -973) {
            // значение поля должно быть больше -973
            return false;
        }
        return true;
    }
}
