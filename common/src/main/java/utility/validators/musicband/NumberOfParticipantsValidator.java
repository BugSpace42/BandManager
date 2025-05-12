package main.java.utility.validators.musicband;

import main.java.utility.validators.Validator;

/**
 * Проверка корректности количества участников музыкальной группы.
 * @author Alina
 */
public class NumberOfParticipantsValidator  implements Validator<Integer>{
    @Override
    public boolean validate(Integer numberOfParticipants) {
        // значение поля должно быть больше 0
        return numberOfParticipants > 0;
    }
}
