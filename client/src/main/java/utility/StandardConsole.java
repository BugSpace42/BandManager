package main.java.utility;

/**
 * Стандартная консоль для ввода команд и вывода результата.
 * @author Alina
 */
public class StandardConsole implements Console{
    /**
     * Выводит в консоль строковое представление объекта.
     * @param o объект, который нужно вывести
     */
    @Override
    public void print(Object o) {
        System.out.print(o);
    }

    /**
     * Выводит в консоль строковое представление объекта с переносом строки.
     * @param o объект, который нужно вывести
     */
    @Override
    public void println(Object o) {
        System.out.println(o);
    }

    /**
     * Выводит в консоль сообщение об ошибке.
     * @param o ошибка, которую нужно вывести
     */
    @Override
    public void printError(Object o) {
        System.out.println("Ошибка:" + o);
    }
}
