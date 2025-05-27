package main.java.utility.entityaskers;

import entity.Album;
import main.java.exceptions.CanceledCommandException;
import main.java.managers.ConsoleManager;
import utility.builders.AlbumBuilder;
import utility.validators.musicband.bestalbum.AlbumNameValidator;
import utility.validators.musicband.bestalbum.AlbumSalesValidator;

/**
 * Отвечает за запрос у пользователя музыкального альбома.
 * @author Alina
 */
public class AlbumAsker {
    /**
     * Запрашивает у пользователя название музыкального альбома.
     * @return введённое название музыкального альбома.
     */
    public static String askAlbumName() throws CanceledCommandException {
        ConsoleManager.println("Введите название музыкального альбома (при наличии).");
        ConsoleManager.println("Название музыкального альбома не должно быть пустым и не должно содержать кавычки.");
        String name = ConsoleManager.askObject();
        if (name == null) {
            name = null;
        }
        else if (name.isBlank()) {
            name = null;
        }
        else {
            AlbumNameValidator validator = new AlbumNameValidator();
            if (!validator.validate(name)) {
                ConsoleManager.println("Введено некорректное название музыкального альбома.");
                ConsoleManager.println("Название музыкального альбома не должно быть пустым и не должно содержать кавычки.");
                ConsoleManager.println("Попробуйте снова.");
                // запрашиваем у пользователя данные, пока не введёт подходящие
                name = askAlbumName();
            }
        }
        return name;
    }

    /**
     * Запрашивает у пользователя продажи музыкального альбома.
     * @return введённые продажи музыкального альбома.
     */
    public static Double askAlbumSales() throws CanceledCommandException {
        ConsoleManager.println("Введите число продаж музыкального альбома.");
        ConsoleManager.println("Число продаж должно быть числом типа Double, большим чем 0.");
        String salesString = ConsoleManager.askObject();
        Double sales;
        AlbumSalesValidator validator = new AlbumSalesValidator();
        try {
            sales = Double.valueOf(salesString);
        } catch (NumberFormatException e) {
            ConsoleManager.println("Введённая строка не является числом типа Double.");
            ConsoleManager.println("Попробуйте снова.");
            sales = askAlbumSales();
        }
        if (validator.validate(sales)) {
            return sales;
        }
        else {
            ConsoleManager.println("Введено некорректные продажи музыкального альбома.");
            ConsoleManager.println("Попробуйте снова.");
            // запрашиваем у пользователя данные, пока не введёт подходящие
            sales = askAlbumSales();
        }
        return sales;
    }

    /**
     * Запрашивает у пользователя объект класса Album.
     * @return введённый объект класса Album
     * @throws CanceledCommandException
     */
    public static Album askAlbum() throws CanceledCommandException {
        Album album;
        String name = askAlbumName();
        if (name == null) {
            album = null;
        }
        else {
            Double sales = askAlbumSales();
            album = AlbumBuilder.build(name, sales);
        }
        return album;
    }
}
