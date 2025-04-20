package utility.validators.musicband;

import utility.validators.Validator;

/**
 * Проверка корректности ID музыкальной группы.
 * @author Alina
 */
public class IdValidator implements Validator<Long>{
    @Override
    public boolean validate(Long id) {
        if (id <= 0) {
            // значение поля должно быть больше 0
            return false;
        }
        return true;
    }
}
