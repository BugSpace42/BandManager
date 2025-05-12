package main.java.managers;

import exceptions.CanceledCommandException;
import main.java.utility.Console;
import main.java.utility.StandardConsole;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс, отвечающий за взаимодействие с пользователем в консоли.
 * @author Alina
 */
public class ConsoleManager {
    private static ConsoleManager consoleManager;
    private static Console console = new StandardConsole();
    private static Scanner scanner;

    private ConsoleManager() {
        updateScanner();
    }

    /**
     * Метод, использующийся для получения ConsoleManager.
     * Создаёт новый объект, если текущий объект ещё не создан.
     * @return consoleManager
     */
    public static ConsoleManager getConsoleManager() {
        if (consoleManager == null) {
            consoleManager = new ConsoleManager();
        }
        return consoleManager;
    }

    /**
     * Выводит в консоль строковое представление объекта.
     * @param o объект, который нужно вывести
     */
    public static void print(Object o) {
        console.print(o);
    }

    /**
     * Выводит в консоль строковое представление объекта с переносом строки.
     * @param o объект, который нужно вывести
     */
    public static void println(Object o) {
        console.println(o);
    }

    /**
     * Выводит в консоль сообщение об ошибке.
     * @param o ошибка, которую нужно вывести
     */
    public static void printError(Object o) {
        console.println(o);
    }

    /**
     * Приветствует пользователя.
     */
    public static void greeting(){
        console.println("Добро пожаловать в приложение, созданное для управления коллекцией объектов класса MusicBand.");
        console.println("Для получения информации о доступных командах введите help");
    }

    /**
     * Запрашивает у пользователя и считывает команду.
     * @return список, состоящий из названия команды и введённых аргументов
     */
    public static String[] askCommand() {
        try {
            console.println("Введите команду: ");
            console.print("> ");
            String line = scanner.nextLine();
            if (line == null) {
                throw new NoSuchElementException();
            }
            String [] text = line.trim().split(" ");
            return text;
        } catch (NoSuchElementException e) {
            updateScanner();
            return null;
        }
    }

    /**
     * Запрашивает у пользователя и считывает объект.
     * @return строка, введённая пользователем
     * @throws CanceledCommandException исключение, выбрасываемое, если пользователь отменил выполнение текущей команды
     */
    public static String askObject() throws CanceledCommandException {
        console.print("> ");
        try {
            String text = scanner.nextLine();
            if (text == null) {
                throw new NoSuchElementException();
            }
            else {
                text = text.trim();
            }
            return text;
        } catch (NoSuchElementException e) {
            updateScanner();
            throw new CanceledCommandException("Обнаружен конец потока.");
        }
    }

    /**
     * Считывает команду.
     * @return список, состоящий из названия команды и аргументов
     */
    public static String[] readCommand() {
        try {
            String [] text = scanner.nextLine().trim().split(" ");
            return text;
        } catch (NoSuchElementException e) {
            printError("Обнаружен конец потока.");
            return null;
        }
    }

    /**
     * Считывает объект.
     * @return считанная строка
     */
         public static String readObject() {
        try {
            String text = scanner.nextLine().trim();
            return text;
        } catch (NoSuchElementException e) {
            printError("Обнаружен конец потока.");
            return null;
        }
    }

    /**
     * Создаёт новый сканер.
     */
    private static void updateScanner() {
        print("\n");
        ConsoleManager.scanner = new Scanner(System.in, "Cp866");
    }

    /**
     * Заменяет информацию о консоли.
     * @param console новая консоль
     */
    public static void setConsole(Console console) {
        ConsoleManager.console = console;
    }

    /**
     * Заменяет информацию о сканере.
     * @param scanner новый сканер
     */
    public static void setScanner(Scanner scanner) {
        ConsoleManager.scanner = scanner;
    }

    /**
     * Возвращает информацию о консоли.
     * @return текущая консоль
     */
    public static Console getConsole() {
        return console;
    }

    /**
     * Возвращает информацию о сканере.
     * @return текущий сканер
     */
    public static Scanner getScanner() {
        return scanner;
    }
}
