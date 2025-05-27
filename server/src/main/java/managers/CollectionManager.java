package main.java.managers;

import entity.MusicBand;

import java.util.*;

/**
 * Класс, который оперирует коллекцией.
 * @author Alina
 */
public class CollectionManager {
    private static CollectionManager collectionManager;
    private static HashMap<Integer, MusicBand> collection = new HashMap<>();
    private static Date initDate;

    private CollectionManager() {}

    /**
     * Метод, использующийся для получения CollectionManager.
     * Создаёт новый объект, если текущий объект ещё не создан.
     * @return collectionManager
     */
    public static CollectionManager getCollectionManager() {
        if (collectionManager == null) {
            collectionManager = new CollectionManager();
            initDate = new Date();
        }
        return collectionManager;
    }

    /**
    * @return коллекция
    */
    public static HashMap<Integer, MusicBand> getCollection() {
        return collection;
    }

    /**
     * Возвращает элемент коллекции с заданным id.
     * @param id id элемента
     * @return элемент коллекции
     */
    public MusicBand getById(Long id) {
        for (HashMap.Entry<Integer, MusicBand> entry : collection.entrySet()) {
          if (entry.getValue().getId().equals(id)) return entry.getValue();
        }
        return null;
      }

    /**
     * Добавляет элемент в коллекцию.
     * @param musicBand добавляемый объект
     */
    public void addToCollection(Integer key, MusicBand musicBand) {
        collection.put(key, musicBand);
    }

    /**
     * Обновляет элемент коллекции, id которого равен заданному.
     * @param id id элемента, значение которого нужно обновить
     * @param musicBand новое значение элемента
     */
    public void updateElementById(Long id, MusicBand musicBand) {
        for (HashMap.Entry<Integer, MusicBand> entry : collection.entrySet()) {
            if (entry.getValue().getId().equals(id)) collection.put(entry.getKey(), musicBand);
        }
    }

    /**
     * Удаляет элемент из коллекции по его ключу.
     * @param key ключ элемента, который нужно удалить
     */
    public void removeByKey(Integer key) {
        collection.remove(key);
    }

    /**
     * Очищает коллекцию.
     */
    public void clearCollection() {
        collection.clear();
    }

    /**
     * Проверяет наличие элемента с заданным ключом в коллекции
     * @param key ключ элемента, наличие которого нужно проверить
     * @return true, если элемент с заданным ключом содержится в коллекции, false - иначе
     */
    public boolean containsKey(Integer key) {
        return collection.containsKey(key);
    }

    /**
     * Возвращает дату инициализации коллекции.
     * @return дата инициализации коллекции
     */
    public Date getInitDate() {
        return initDate;
    }

    /**
     * Возвращает список id существующих элементов коллекции.
     * @return список id существующих элементов коллекции
     */
    public static List<Long> getIdList() {
        ArrayList<Long> idList = new ArrayList<>();
        for (HashMap.Entry<Integer, MusicBand> entry : collection.entrySet()) {
            idList.add(entry.getValue().getId());
        }
        return idList;
    }

    /**
     * Возвращает список ключей существующих элементов коллекции.
     * @return список ключей существующих элементов коллекции
     */
    public static List<Integer> getKeyList() {
        List<Integer> keyList = new ArrayList<>();
        for (HashMap.Entry<Integer, MusicBand> entry : collection.entrySet()) {
            keyList.add(entry.getKey());
        }
        return keyList;
    }

    /**
     * Задаёт новое значение полю коллекции.
     * @param collection новая коллекция.
     */
    public static void setCollection(HashMap<Integer, MusicBand> collection) {
        CollectionManager.collection = collection;
    }

    /**
     * Возвращает строковое представление коллекции.
     * @return строковое представление коллекции
     */
    @Override
    public String toString() {
        String info = "";
        for (Map.Entry<Integer, MusicBand> elem : collection.entrySet()) {
            info += "Элемент коллекции с ключом " + elem.getKey() + ":\n";
            info += elem.getValue();
            info += "\n";
        }
        if (info.length() > 0) {
            info = info.substring(0, info.length()-1);
        }
        return info;
    }
}
