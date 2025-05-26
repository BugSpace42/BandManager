package utility.validators.musicband;

import utility.validators.Validator;

/**
 * Проверка корректности ключа музыкальной группы.
 * @author Alina
 */
public class KeyValidator implements Validator<Integer>{
    @Override
    public boolean validate(Integer key) {
        // значение поля должно целым числом, и всё
        // мы уже передаём Integer, поэтому это всегда правда
        // оставляю валидатор на случай, если понадобится какая-то ещё проверка
        return true;
    }
}
