package utility.validators.stringvalidators.musicband.coordinates;

import utility.validators.Validator;

/**
 * Проверка корректности координаты y.
 * @author Alina
 */
public class CoordYValidator implements Validator<String>{
    @Override
    public boolean validate(String yString) {
        try {
            Long y = Long.valueOf(yString);
            if (y <= -973) {
                // значение поля должно быть больше -973
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
