package main.java.utility.entityaskers;

import entity.MusicGenre;
import exceptions.CanceledCommandException;
import main.java.managers.ConsoleManager;
import utility.builders.MusicGenreBuilder;

/**
 * Отвечает за запрос у пользователя музыкального жанра.
 * @author Alina
 */
public class MusicGenreAsker {
    /**
     * Запрашивает у пользователя музыкальный жанр.
     * @return введённый музыкальный жанр.
     */
    public static MusicGenre askMusicGenre() throws CanceledCommandException {
        ConsoleManager.println("Введите музыкальный жанр группы (при наличии).");
        ConsoleManager.println("Список музыкальных жанров: " + MusicGenre.names());
        String genreString = ConsoleManager.askObject();
        MusicGenre genre;
        if (genreString == null) {
            genre = null;
        }
        else if (genreString.isBlank()) {
            genre = null;
        }
        else {
            try {
                genre = MusicGenreBuilder.build(genreString);
            } catch (IllegalArgumentException e) {
                ConsoleManager.println("Введён некорректный музыкальный жанр.");
                ConsoleManager.println("Попробуйте снова.");
                // запрашиваем у пользователя данные, пока не введёт подходящие
                genre = askMusicGenre();
            }
        }
        return genre;
    }
}
