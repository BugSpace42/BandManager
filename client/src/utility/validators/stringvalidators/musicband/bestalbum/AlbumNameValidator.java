package utility.validators.stringvalidators.musicband.bestalbum;

import utility.validators.Validator;

/**
 * Проверка корректности названия альбома.
 * @author Alina
 */
public class AlbumNameValidator implements Validator<String>{
    @Override
    public boolean validate(String name) {
        if (name.isEmpty()) {
            // строка пустая или null
            return false;
        }
        if (name.contains("\"")) {
            // строка содержит кавычки
            return false;
        }
        return true;
    }
}
