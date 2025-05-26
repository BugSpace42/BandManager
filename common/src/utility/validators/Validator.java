package utility.validators;

/**
 * Проверяет корректность данных определённого типа, введённых пользователем.
 * @author Alina
 */
public interface Validator<T> {
    /**
     * Проверяет корректность данных, введённых пользователем
     * @param value значение для проверки
     * @return true - если значение корректное, иначе false
     */
    boolean validate(T value);
}
