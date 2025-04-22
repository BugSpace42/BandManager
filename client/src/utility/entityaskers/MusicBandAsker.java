package utility.entityaskers;

import entity.Album;
import entity.Coordinates;
import entity.MusicBand;
import entity.MusicGenre;
import exceptions.CanceledCommandException;
import managers.ConsoleManager;
import utility.builders.MusicBandBuilder;
import utility.validators.musicband.NameValidator;
import utility.validators.musicband.NumberOfParticipantsValidator;

/**
 * Отвечает за запрос у пользователя музыкальной группы.
 * @author Alina
 */
public class MusicBandAsker {
    /**
     * Запрашивает у пользователя название музыкальной группы.
     * @return введённое название музыкальной группы.
     * @throws CanceledCommandException
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
     * @throws CanceledCommandException
     */
    public static Integer askMusicBandNumber() throws CanceledCommandException {
        ConsoleManager.println("Введите количество участников музыкальной группы.");
        ConsoleManager.println("Количество участников музыкальной группы должно быть числом типа Integer, большим чем 0.");
        Integer numberOfParticipants;
        String numberSrting = ConsoleManager.askObject();
        NumberOfParticipantsValidator validator = new NumberOfParticipantsValidator();
        try {
            numberOfParticipants = Integer.valueOf(numberSrting);
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

        MusicBand musicBand = MusicBandBuilder.build(name, coordinates, numberOfParticipants, genre, bestAlbum);
        return musicBand;
    }
}
