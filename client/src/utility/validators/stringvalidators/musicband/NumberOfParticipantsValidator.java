package utility.validators.stringvalidators.musicband;

import utility.validators.Validator;

/**
 * Проверка корректности количества участников музыкальной группы, лежащего в строке.
 * @author Alina
 */
public class NumberOfParticipantsValidator  implements Validator<String>{
    @Override
    public boolean validate(String numberOfParticipantsString) {
        try {
            Integer numberOfParticipants = Integer.valueOf(numberOfParticipantsString);
            if (numberOfParticipants <= 0) {
                // значение поля должно быть больше 0
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
