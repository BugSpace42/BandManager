package utility.validators.stringvalidators.musicband;

import utility.validators.Validator;

import java.util.Date;

/**
 * Проверка корректности времени создания объекта класса MusicBand, лежащего в строке.
 * @author Alina
 */
public class CreationDateValidator implements Validator<String>{
    @Override
    public boolean validate(String creationDateString) {
        try {
            Date creationDate = new Date(Long.parseLong(creationDateString));
            Date currentDate = new Date();
            if (creationDate.after(currentDate)) {
                // значение поля должно быть меньше текущего времени
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
