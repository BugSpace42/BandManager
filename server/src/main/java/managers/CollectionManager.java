package main.java.managers;

import entity.MusicBand;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Date;

/**
 * Класс, который оперирует коллекцией.
 * @author Alina
 */
public class CollectionManager {
    private static CollectionManager instance;
    private static HashMap<Integer, MusicBand> collection;
    private static HashMap<Integer, String> musicBandOwners = new HashMap<>();
    private static Date initDate;

    private CollectionManager() {
        collection = new HashMap<>();
        initDate = new Date();
    }

    /**
     * Метод, использующийся для получения CollectionManager.
     * Создаёт новый объект, если текущий объект ещё не создан.
     * @return collectionManager
     */
    public static CollectionManager getCollectionManager() {
        if (instance == null) {
            instance = new CollectionManager();
        }
        return instance;
    }

    /**
     * @return коллекция
     */
    public static HashMap<Integer, MusicBand> getCollection() {
        synchronized (collection) {
            return new HashMap<>(collection);
        }
    }

    /**
     * Добавляет элемент в коллекцию.
     * @param key ключ музыкальной группы
     * @param musicBand добавляемый объект
     */
    public static void addToCollection(Integer key, MusicBand musicBand) {
        synchronized (collection) {
            collection.put(key, musicBand);
        }
    }

    public static void addOwnerToCollection(Integer key, String owner) {
        synchronized (musicBandOwners) {
            musicBandOwners.put(key, owner);
        }
    }

    /**
     * Удаляет элемент из коллекции по его ключу.
     * @param key ключ элемента, который нужно удалить
     */
    public static void removeByKey(Integer key) {
        synchronized (collection) {
            synchronized (musicBandOwners) {
                collection.remove(key);
                musicBandOwners.remove(key);
            }
        }
    }

    /**
     * Очищает коллекцию.
     */
    public static void clear() {
        synchronized (collection) {
            synchronized (musicBandOwners) {
                collection.clear();
                musicBandOwners.clear();
            }
        }
    }

    /**
     * Возвращает элемент коллекции с заданным id.
     * @param id id элемента
     * @return элемент коллекции
     */
    public MusicBand getById(Long id) {
        synchronized (collection) {
            for (HashMap.Entry<Integer, MusicBand> entry : collection.entrySet()) {
                if (entry.getValue().getId().equals(id)) return entry.getValue();
            }
            return null;
        }
    }

    /**
     * Возвращает ключ элемента коллекции с заданным id.
     * @param id id элемента
     * @return ключ элемента коллекции
     */
    public Integer getKeyById(Long id) {
        synchronized (collection) {
            for (HashMap.Entry<Integer, MusicBand> entry : collection.entrySet()) {
                if (entry.getValue().getId().equals(id)) return entry.getKey();
            }
            return null;
        }
    }

    /**
     * Обновляет элемент коллекции, id которого равен заданному.
     * @param id id элемента, значение которого нужно обновить
     * @param musicBand новое значение элемента
     */
    public void updateElementById(Long id, MusicBand musicBand) {
        synchronized (collection) {
            for (HashMap.Entry<Integer, MusicBand> entry : collection.entrySet()) {
                if (entry.getValue().getId().equals(id)) collection.put(entry.getKey(), musicBand);
            }
        }
    }

    /**
     * Обновляет элемент коллекции, id которого равен заданному.
     * @param key ключ элемента, значение которого нужно обновить
     * @param musicBand новое значение элемента
     */
    public void updateElementByKey(Integer key, MusicBand musicBand) {
        synchronized (collection) {
            for (HashMap.Entry<Integer, MusicBand> entry : collection.entrySet()) {
                if (entry.getKey().equals(key)) collection.put(entry.getKey(), musicBand);
            }
        }
    }

    /**
     * Проверяет наличие элемента с заданным ключом в коллекции
     * @param key ключ элемента, наличие которого нужно проверить
     * @return true, если элемент с заданным ключом содержится в коллекции, false - иначе
     */
    public boolean containsKey(Integer key) {
        synchronized (collection) {
            return collection.containsKey(key);
        }
    }

    public static boolean checkOwner(String owner, Integer key) {
        synchronized (musicBandOwners) {
            if (musicBandOwners.containsKey(key)) {
                return musicBandOwners.get(key).equals(owner);
            }
        }
        return false;
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
        synchronized (collection) {
            return collection.values().stream()
                    .map(MusicBand::getId)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Возвращает список ключей существующих элементов коллекции.
     * @return список ключей существующих элементов коллекции
     */
    public static List<Integer> getKeyList() {
        synchronized (collection) {
            return collection.keySet().stream().collect(Collectors.toList());
        }
    }

    /**
     * Задаёт новое значение полю коллекции.
     */
    public static void setCollection(HashMap<Integer, MusicBand> newCollection) {
        synchronized (collection) {
            collection = newCollection;
        }
    }

    public static void setMusicBandOwners(HashMap<Integer, String> musicBandOwners) {
        synchronized (musicBandOwners) {
            CollectionManager.musicBandOwners = musicBandOwners;
        }
    }

    public static HashMap<Integer, String> getMusicBandOwners() {
        synchronized (musicBandOwners) {
            return new HashMap<>(CollectionManager.musicBandOwners);
        }
    }

    /**
     * Возвращает строковое представление коллекции.
     * @return строковое представление коллекции
     */
    @Override
    public String toString() {
        synchronized (collection) {
            StringBuilder info = new StringBuilder();
            for (Map.Entry<Integer, MusicBand> elem : collection.entrySet()) {
                info.append("Элемент коллекции с ключом ").append(elem.getKey()).append(":\n");
                info.append(elem.getValue());
            }
            return info.toString();
        }
    }
}
