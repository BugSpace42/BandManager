package main.java.utility.validators;

import exceptions.WrongValueException;
import main.java.exceptions.AskingArgumentsException;
import main.java.managers.Runner;
import utility.Types;
import utility.validators.SpecificTypeValidator;
import utility.validators.musicband.IdValidator;
import utility.validators.musicband.KeyValidator;
import utility.validators.musicband.NumberOfParticipantsValidator;

/**
 * Проверка строки на содержание объекта определённого типа.
 * @author Alina
 */
public class TypeValidator {
    public static boolean isTypeValid(Types type, String value) throws AskingArgumentsException {
        try{
            switch (type) {
                case INT -> SpecificTypeValidator.isInteger(value);
                case LONG -> SpecificTypeValidator.isLong(value);
                case SHORT -> SpecificTypeValidator.isShort(value);
                case DOUBLE -> SpecificTypeValidator.isDouble(value);
                case BOOLEAN -> SpecificTypeValidator.isBoolean(value);
                case MUSIC_BAND_KEY -> {
                    Integer key = Integer.valueOf(value);
                    if (! new KeyValidator().validate(key)) {
                        return false;
                    }
                }
                case MUSIC_BAND_KEY_UNIQUE -> {
                    Integer key = Integer.valueOf(value);
                    if (! new KeyValidator().validate(key)) {
                        return false;
                    }
                    if (Runner.getRunner().getKeyList().contains(key)) {
                        throw new AskingArgumentsException("Полученный ключ уже содержится в коллекции. Ожидался уникальный ключ.");
                    }
                }
                case MUSIC_BAND_KEY_CONTAINED -> {
                    Integer key = Integer.valueOf(value);
                    if (! new KeyValidator().validate(key)) {
                        return false;
                    }
                    if (! Runner.getRunner().getKeyList().contains(key)) {
                        throw new AskingArgumentsException("Полученный ключ не содержится в коллекции. Ожидался ключ, содержащийся в коллекции.");
                    }
                }
                case MUSIC_BAND_ID -> {
                    Long id = Long.valueOf(value);
                    if (! new IdValidator().validate(id)) {
                        return false;
                    }
                }
                case MUSIC_BAND_NUMBER -> {
                    Integer number = Integer.valueOf(value);
                    if (! new NumberOfParticipantsValidator().validate(number)) {
                        return false;
                    }
                }
                // В остальных случаях значения либо строки, поэтому всегда валидные,
                // либо не могут лежать в одной строке
            }
            return true;
        } catch (NumberFormatException e) {
            throw new AskingArgumentsException("Получен некорректный аргумент. Ожидался аргумент типа " + type);
        }
    }
}
