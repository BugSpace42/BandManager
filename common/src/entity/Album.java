package entity;

import java.io.Serializable;

/**
 * Класс альбома.
 * @author Alina
 */
public class Album implements Comparable<Album>, Serializable {
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Double sales; //Поле может быть null, Значение поля должно быть больше 0

    /**
     * Конструктор - создание нового объекта с заданными именем и продажами.
     * @param name название альбома
     * @param sales продажи альбома
     */
    public Album(String name, Double sales) {
        this.name = name;
        this.sales = sales;
    }

    public String getName() {
        return name;
    }

    public Double getSales(){
        return sales;
    }

    /**
     * Проверка равенства двух музыкальных альбомов.
     * @param album альбом для сравнения
     * @return true - если объекты одинаковые, false - если разные
     */
    public boolean equals(Album album) {
        if (album == this) return true;
        if (album == null) return false;
        return album.name.equals(this.name) && album.sales.equals(this.sales);
    }

    @Override
    public int compareTo(Album album) {
        return this.name.compareTo(album.name);
    }

    /**
     * @return Строковое представление объекта класса.
     */
    @Override
    public String toString() {
        return String.format("Название: %s \n Объём продаж: %.2f", name, sales);
    }
}