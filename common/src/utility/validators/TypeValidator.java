package utility.validators;

import utility.Types;
import utility.validators.musicband.IdValidator;
import utility.validators.musicband.KeyValidator;
import utility.validators.musicband.NumberOfParticipantsValidator;

import java.util.Date;

/**
 * Проверка строки на содержание объекта определённого типа.
 * @author Alina
 */
public class TypeValidator {
    public static boolean isTypeValid(Types type, String value) {
        try {
            switch (type) {
                case INT -> isInteger(value);
                case LONG -> isLong(value);
                case SHORT -> isShort(value);
                case DOUBLE -> isDouble(value);
                case BOOLEAN -> isBoolean(value);
                case MUSIC_BAND_KEY -> {
                    Integer key = Integer.valueOf(value);
                    if (! new KeyValidator().validate(key)) {
                        return false;
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
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет, является ли строка числом типа int
     * @param value строка для проверки
     * @return true - если является, false - иначе
     */
    public static boolean isIntPrim(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет, является ли строка числом типа Integer
     * @param value строка для проверки
     * @return true - если является, false - иначе
     */
    public static boolean isInteger(String value) {
        try {
            Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет, является ли строка числом типа long
     * @param value строка для проверки
     * @return true - если является, false - иначе
     */
    public static boolean isLongPrim(String value) {
        try {
            Long.parseLong(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет, является ли строка числом типа Long
     * @param value строка для проверки
     * @return true - если является, false - иначе
     */
    public static boolean isLong(String value) {
        try {
            Long.valueOf(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет, является ли строка числом типа Short
     * @param value строка для проверки
     * @return true - если является, false - иначе
     */
    public static boolean isShort(String value) {
        try {
            Short.valueOf(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет, является ли строка числом типа float
     * @param value строка для проверки
     * @return true - если является, false - иначе
     */
    public static boolean isFloatPrim(String value) {
        try {
            Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет, является ли строка числом типа Float
     * @param value строка для проверки
     * @return true - если является, false - иначе
     */
    public static boolean isFloat(String value) {
        try {
            Float.valueOf(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет, является ли строка числом типа double
     * @param value строка для проверки
     * @return true - если является, false - иначе
     */
    public static boolean isDoublePrim(String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет, является ли строка числом типа Double
     * @param value строка для проверки
     * @return true - если является, false - иначе
     */
    public static boolean isDouble(String value) {
        try {
            Double.valueOf(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет, является ли строка значением типа boolean или Boolean
     * @param value строка для проверки
     * @return true - если является, false - иначе
     */
    public static boolean isBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }

    /**
     * Проверяет, является ли строка значением типа Date
     * @param value строка для проверки
     * @return true - если является, false - иначе
     */
    public static boolean isDate(String value) {
        return isLongPrim(value);
    }

    /**
     * Проверяет, является ли строка значением типа Date, которое было в прошлом
     * @param value строка для проверки
     * @return true - если является, false - иначе
     */
    public static boolean isDatePast(String value) {
        try {
            Date creationDate = new Date(Long.parseLong(value));
            Date currentDate = new Date();
            if (creationDate.after(currentDate)) {
                // значение поля должно быть меньше текущего времени
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
