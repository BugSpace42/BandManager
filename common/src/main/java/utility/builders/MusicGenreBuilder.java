package main.java.utility.builders;

import entity.MusicGenre;

/**
 * Управляет сборкой объекта класса MusicGenre 
 * @author Alina
 */
public class MusicGenreBuilder {
    /**
     * Собирает объект класса MusicGenre
     * @param genre музыкальный жанр
     * @return объект класса MusicGenre
     */
    public static MusicGenre build(String genre) {
        genre = genre.trim().toUpperCase();
        return MusicGenre.valueOf(genre);
    }
}
