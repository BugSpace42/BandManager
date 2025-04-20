package utility.validators.stringvalidators.musicband;

import utility.validators.Validator;

/**
 * Проверка корректности названия музыкальной группы.
 * @author Alina
 */
public class NameValidator implements Validator<String>{
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
