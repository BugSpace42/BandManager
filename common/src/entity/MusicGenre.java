package entity;

import utility.validators.TypeValidator;

import java.io.Serializable;

/**
 * Перечисление музыкальных жанров.
 * @author Alina
 */
public enum MusicGenre implements Serializable {
    ROCK,
    HIP_HOP,
    JAZZ,
    POP,
    PUNK_ROCK;

    /**
     * @return Строка со всеми элементами enum'а через запятую.
     */
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (MusicGenre genre : values()) {
            nameList.append(genre.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}