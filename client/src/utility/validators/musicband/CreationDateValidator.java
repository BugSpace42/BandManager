package utility.validators.musicband;

import utility.validators.Validator;

import java.util.Date;

/**
 * Проверка корректности времени создания объекта класса MusicBand.
 * @author Alina
 */
public class CreationDateValidator implements Validator<Date>{
    @Override
    public boolean validate(Date creationDate) {
        Date currentDate = new Date();
        if (creationDate.after(currentDate)) {
            // значение поля должно быть меньше текущего времени
            return false;
        }
        return true;
    }
}
