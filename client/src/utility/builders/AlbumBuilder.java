package utility.builders;

import entity.Album;

/**
 * Управляет сборкой объекта класса Album 
 * @author Alina
 */
public class AlbumBuilder {
    /**
     * Собирает объект класса Album
     * @param name название альбома
     * @param sales продажи альбома
     * @return объект класса Album
     */
    public static Album build(String name, Double sales) {
        Album album = new Album(name, sales);
        return album;
    }
}