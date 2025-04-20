package entity;

import exceptions.CanceledCommandException;
import managers.ConsoleManager;
import utility.builders.MusicBandBuilder;
import utility.validators.musicband.NameValidator;
import utility.validators.musicband.NumberOfParticipantsValidator;

/**
 * Класс музыкальной группы.
 * @author Alina
 */
public class MusicBand implements Comparable<MusicBand>{
    private final Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    private final java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final Integer numberOfParticipants; //Поле не может быть null, Значение поля должно быть больше 0
    private final MusicGenre genre; //Поле может быть null
    private final Album bestAlbum; //Поле может быть null

    /**
     * Конструктор - создание нового объекта с заданными параметрами.
     * @param id автоматически сгенерированный id музыкальной группы
     * @param name название музыкальной группы
     * @param coordinates местоположение музыкальной группы
     * @param creationDate автоматически сгенерированное время создания объекта
     * @param numberOfParticipants количество участников музыкальной группы
     * @param genre жанр музыкальной группы
     * @param bestAlbum лучший альбом музыкальной группы
     */
    public MusicBand(Long id, String name, Coordinates coordinates, java.util.Date creationDate, 
                    Integer numberOfParticipants, MusicGenre genre, Album bestAlbum) {
        this.id=id;
        this.name=name;
        this.coordinates=coordinates;
        this.creationDate=creationDate;
        this.numberOfParticipants=numberOfParticipants;
        this.genre=genre;
        this.bestAlbum=bestAlbum;
    }
    
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
        Coordinates coordinates = Coordinates.askCoordinates();
        Integer numberOfParticipants = askMusicBandNumber();
        MusicGenre genre = MusicGenre.askMusicGenre();
        Album bestAlbum = Album.askAlbum();

        MusicBand musicBand = MusicBandBuilder.build(name, coordinates, numberOfParticipants, genre, bestAlbum);
        return musicBand;
    }

    /**
     * @return id музыкальной группы
     */
    public Long getId() {
        return id;
    }

    /**
     * @return название музыкальной группы
     */
    public String getName() {
        return name;
    }

    /**
     * @return местоположение музыкальной группы
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return время создания объекта
     */
    public java.util.Date getCreationDate() {
        return creationDate;
    }

    /**
     * @return количество участников музыкальной группы
     */
    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    /**
     * @return жанр музыкальной группы
     */
    public MusicGenre getGenre() {
        return genre;
    }

    /**
     * @return лучший альбом музыкальной группы
     */
    public Album getBestAlbum() {
        return bestAlbum;
    }

    /**
     * Проверка равенcтва двух музыкальных групп.
     * @param musicBand музыкальная группа для сравнения
     * @return true - если объекты одинаковые, false - если разные
     */
    public boolean equals(MusicBand musicBand) {
        if (musicBand == this) return true;
        return musicBand.id.equals(id) && 
               musicBand.name.equals(name) &&
               musicBand.coordinates.equals(coordinates) &&
               musicBand.numberOfParticipants.equals(numberOfParticipants) &&
               musicBand.genre.equals(genre) &&
               musicBand.bestAlbum.equals(bestAlbum);
    }

    /**
     * Функция сравнения двух музыкальных групп.
     */
    @Override
    public int compareTo(MusicBand musicBand) {
        return this.name.compareTo(musicBand.name);
    }
    
    /**
     * @return Строковое представление объекта класса.
     */
    @Override
    public String toString() {
        String info = "";
        info += "Музыкальная группа № " + id;
        info += "\n Название: " + name;
        info += "\n Местоположение: " + coordinates;
        info += "\n Дата создания объекта: " + creationDate;
        info += "\n Количество участников: " + numberOfParticipants;
        info += "\n Жанр: " + genre;
        info += "\n Лучший альбом: " + bestAlbum;
        return info;
    }
}
