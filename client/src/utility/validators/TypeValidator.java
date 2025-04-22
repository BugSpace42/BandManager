package utility.validators;

import java.util.Date;

/**
 * Проверка строки на содержание объекта определённого типа.
 * @author Alina
 */
public class TypeValidator {
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
