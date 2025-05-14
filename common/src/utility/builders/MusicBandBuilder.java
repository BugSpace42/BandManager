package utility.builders;

import entity.Album;
import entity.Coordinates;
import entity.MusicBand;
import entity.MusicGenre;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Управляет сборкой объекта класса MusicBand 
 * @author Alina
 */
public class MusicBandBuilder {
    private static Long currentId = 1L;

    /**
     * Увеличивает счётчик id музыкальных групп на 1.
     * @return текущее id
     */
    private static Long nextId() {
        return currentId++;
    }

    /**
     * Собирает объект класса MusicBand.
     * @param name название музыкальной группы
     * @param coordinates местоположение музыкальной группы
     * @param numberOfParticipants количество участников музыкальной группы
     * @param genre жанр музыкальной группы
     * @param bestAlbum лучший альбом музыкальной группы
     * @return объект класса MusicBand
     */
    public static MusicBand build(String name, Coordinates coordinates, Integer numberOfParticipants, MusicGenre genre, Album bestAlbum) {
        Long id;
        id = nextId();
        // List<Long> idList = CollectionManager.getIdList();
        List<Long> idList = new ArrayList<>();
        // TODO
        while(idList.contains(id)) {
            id = nextId();
        }
        Date date = new Date();
        return new MusicBand(id, name, coordinates, date, numberOfParticipants, genre, bestAlbum);
    }
}
