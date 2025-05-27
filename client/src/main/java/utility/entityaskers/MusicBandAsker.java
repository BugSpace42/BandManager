package main.java.utility.entityaskers;

import entity.Album;
import entity.Coordinates;
import entity.MusicBand;
import entity.MusicGenre;
import main.java.exceptions.CanceledCommandException;
import main.java.managers.ConsoleManager;
import utility.builders.MusicBandBuilder;
import utility.validators.musicband.IdValidator;
import utility.validators.musicband.KeyValidator;
import utility.validators.musicband.NameValidator;
import utility.validators.musicband.NumberOfParticipantsValidator;

/**
 * Отвечает за запрос у пользователя музыкальной группы.
 * @author Alina
 */
public class MusicBandAsker {
    /**
     * Запрашивает у пользователя ключ музыкальной группы.
     * @return введённый ключ музыкальной группы.
     */
    public static Integer askMusicBandKey() throws CanceledCommandException {
        ConsoleManager.println("Введите ключ музыкальной группы.");
        ConsoleManager.println("Ключ музыкальной группы должен быть числом типа Integer.");
        Integer key;
        String keyString = ConsoleManager.askObject();
        KeyValidator validator = new KeyValidator();
        try {
            key = Integer.valueOf(keyString);
        } catch (NumberFormatException e) {
            ConsoleManager.println("Введённая строка не является числом типа Integer.");
            ConsoleManager.println("Попробуйте снова.");
            key = askMusicBandNumber();
        }
        if (validator.validate(key)) {
            return key;
        }
        else {
            ConsoleManager.println("Введен некорректный ключ музыкальной группы.");
            ConsoleManager.println("Попробуйте снова.");
            // запрашиваем у пользователя данные, пока не введёт подходящие
            key = askMusicBandKey();
        }
        return key;
    }

    /**
     * Запрашивает у пользователя id музыкальной группы.
     * @return введённый id музыкальной группы.
     */
    public static Long askMusicBandId() throws CanceledCommandException {
        ConsoleManager.println("Введите id музыкальной группы.");
        ConsoleManager.println("Ключ музыкальной группы должен быть числом типа Long, большим чем 0.");
        Long id;
        String idString = ConsoleManager.askObject();
        IdValidator validator = new IdValidator();
        try {
            id = Long.valueOf(idString);
        } catch (NumberFormatException e) {
            ConsoleManager.println("Введённая строка не является числом типа Long.");
            ConsoleManager.println("Попробуйте снова.");
            id = askMusicBandId();
        }
        if (validator.validate(id)) {
            return id;
        }
        else {
            ConsoleManager.println("Введен некорректный id музыкальной группы.");
            ConsoleManager.println("Попробуйте снова.");
            // запрашиваем у пользователя данные, пока не введёт подходящие
            id = askMusicBandId();
        }
        return id;
    }

    /**
     * Запрашивает у пользователя название музыкальной группы.
     * @return введённое название музыкальной группы.
     */
    public static String askMusicBandName() throws CanceledCommandException {
        ConsoleManager.println("Введите название музыкальной группы.");
        ConsoleManager.println("Название музыкальной группы не должно быть пустым и не должно содержать кавычки.");
        String name = ConsoleManager.askObject();
        NameValidator validator = new NameValidator();
        if (validator.validate(name)) {
            return name;
        }
        else {
            ConsoleManager.println("Введено некорректное название музыкальной группы.");
            ConsoleManager.println("Название музыкальной группы не должно быть пустым и не должно содержать кавычки.");
            ConsoleManager.println("Попробуйте снова.");
            // запрашиваем у пользователя данные, пока не введёт подходящие
            name = askMusicBandName();
        }
        return name;
    }

    /**
     * Запрашивает у пользователя количество участников музыкальной группы.
     * @return введённое количество участников музыкальной группы.
     */
    public static Integer askMusicBandNumber() throws CanceledCommandException {
        ConsoleManager.println("Введите количество участников музыкальной группы.");
        ConsoleManager.println("Количество участников музыкальной группы должно быть числом типа Integer, большим чем 0.");
        Integer numberOfParticipants;
        String numberString = ConsoleManager.askObject();
        NumberOfParticipantsValidator validator = new NumberOfParticipantsValidator();
        try {
            numberOfParticipants = Integer.valueOf(numberString);
        } catch (NumberFormatException e) {
            ConsoleManager.println("Введённая строка не является числом типа Integer.");
            ConsoleManager.println("Попробуйте снова.");
            numberOfParticipants = askMusicBandNumber();
        }
        if (validator.validate(numberOfParticipants)) {
            return numberOfParticipants;
        }
        else {
            ConsoleManager.println("Введено некорректное количество участников музыкальной группы.");
            ConsoleManager.println("Попробуйте снова.");
            // запрашиваем у пользователя данные, пока не введёт подходящие
            numberOfParticipants = askMusicBandNumber();
        }
        return numberOfParticipants;
    }

    /**
     * Запрашивает у пользователя объект класса MusicBand.
     * @return введённый объект класса MusicBand
     * @throws CanceledCommandException
     */
    public static MusicBand askMusicBand() throws CanceledCommandException {
        String name = askMusicBandName();
        Coordinates coordinates = CoordinatesAsker.askCoordinates();
        Integer numberOfParticipants = askMusicBandNumber();
        MusicGenre genre = MusicGenreAsker.askMusicGenre();
        Album bestAlbum = AlbumAsker.askAlbum();

        return MusicBandBuilder.build(name, coordinates, numberOfParticipants, genre, bestAlbum);
    }
}
