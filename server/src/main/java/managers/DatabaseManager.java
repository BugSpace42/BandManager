package main.java.managers;

import entity.Album;
import entity.Coordinates;
import entity.MusicBand;
import entity.MusicGenre;
import exceptions.DatabaseException;
import main.java.connection.AbstractTCPServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;

public class DatabaseManager {
    // private static final String url = "jdbc:postgresql://pg:5432/studs"; // если запускаю сразу на helios
    private static final String url = "jdbc:postgresql://127.0.0.1:54321/studs"; // если пробрасываю порты по ssh
    private static final String user = "s409478";
    private static final String password = "KLJNrXjJbVh9iVTm";
    private static Connection connection;
    private static final Logger logger = LogManager.getLogger(AbstractTCPServer.class);

    public static void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            logger.info("Подключение к базе данных прошло успешно.");
        } catch (ClassNotFoundException e) {
            logger.error("Драйвер PostgreSQL не найден.", e);
        } catch (SQLException e) {
            logger.error("Ошибка подключения к базе данных.", e);
        }
    }

    public static void getCollection() {
        HashMap<Integer, MusicBand> collection = new HashMap<>();
        HashMap<Integer, String> ownersCollection = new HashMap<>();
        String sql = "SELECT * FROM music_band";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Integer key = rs.getInt("key");
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                Integer x = rs.getInt("coordinates_x");
                long y = rs.getLong("coordinates_y");
                java.util.Date creationDate = new java.util.Date(rs.getTimestamp("creation_date").getTime());
                int numberOfParticipants = rs.getInt("number_of_participants");
                String genreStr = rs.getString("genre");
                MusicGenre genre = genreStr != null ? MusicGenre.valueOf(genreStr) : null;
                String albumName = rs.getString("best_album_name");
                Double albumSales = rs.getDouble("best_album_sales");
                Album album = albumName != null ? new Album(albumName, albumSales) : null;

                Coordinates coords = new Coordinates(x,y);

                MusicBand musicBand = new MusicBand(id, name, coords, creationDate, numberOfParticipants, genre, album);
                collection.put(key, musicBand);

                String owner = rs.getString("owner_username");
                ownersCollection.put(key, owner);

                logger.info("Элемент считан в коллекцию: {}", musicBand.toString());
            }
            CollectionManager.setCollection(collection);
            CollectionManager.setMusicBandOwners(ownersCollection);
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при чтении коллекции: {}", e.getMessage());
        }
    }

    /**
     * Добавляет объект класса MusicBand в коллекцию
     * @param key ключ, по которому нужно добавить элемент
     * @param musicBand элемент, который нужно добавить
     * @return id добавленного элемента
     * @throws DatabaseException исключение
     */
    public static void addMusicBand(Integer key, MusicBand musicBand) throws DatabaseException {
        String sql = "INSERT INTO music_band (key, name, coordinates_x, coordinates_y, creation_date, " +
                "number_of_participants, genre, best_album_name, best_album_sales, owner_username) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, key);
            pstmt.setString(2, musicBand.getName());
            pstmt.setInt(3, musicBand.getCoordinates().getX());
            pstmt.setLong(4, musicBand.getCoordinates().getY());
            pstmt.setTimestamp(5, new java.sql.Timestamp(musicBand.getCreationDate().getTime()));
            pstmt.setInt(6, musicBand.getNumberOfParticipants());
            pstmt.setString(7, musicBand.getGenre() != null ? musicBand.getGenre().name() : null);
            pstmt.setString(8, musicBand.getBestAlbum() != null ? musicBand.getBestAlbum().getName() : null);
            if (musicBand.getBestAlbum() != null) pstmt.setDouble(9, musicBand.getBestAlbum().getSales());
            else pstmt.setDouble(9, 0);
            pstmt.setString(10, CollectionManager.getMusicBandOwners().get(key));

            logger.info("В базу данных добавлен элемент: {}", musicBand);

            /*
            String sqlId = "SELECT id FROM music_band WHERE key=?";
            PreparedStatement pstmtId = connection.prepareStatement(sqlId);
            pstmtId.setLong(1, key);
            ResultSet rs = pstmtId.executeQuery();
            Long generatedId = null;
            rs.next();
            generatedId = rs.getLong(1);
             */
            //return musicBand.getId();
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при добавлении элемента {}\n Текст ошибки: {}", musicBand, e.getMessage());
            throw new DatabaseException("Произошла ошибка при добавлении элемента " + musicBand);
        }
    }

    public static void updateMusicBandById(Long id, MusicBand musicBand) throws DatabaseException {
        String sql = "UPDATE music_band SET (name, coordinates_x, coordinates_y, " +
                "number_of_participants, genre, best_album_name, best_album_sales) = " +
                "(?, ?, ?, ?, ?, ?, ?) WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, musicBand.getName());
            pstmt.setInt(2, musicBand.getCoordinates().getX());
            pstmt.setLong(3, musicBand.getCoordinates().getY());
            pstmt.setInt(4, musicBand.getNumberOfParticipants());
            pstmt.setString(5, musicBand.getGenre() != null ? musicBand.getGenre().name() : null);
            pstmt.setString(6, musicBand.getBestAlbum() != null ? musicBand.getBestAlbum().getName() : null);
            if (musicBand.getBestAlbum() != null) pstmt.setDouble(9, musicBand.getBestAlbum().getSales());
            else pstmt.setDouble(7, 0);
            pstmt.setLong(8, id);

            pstmt.executeUpdate();
            logger.info("Значение элемента с id {} обновлено на: {}", id, musicBand);
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при обновлении значения элемента с id {}\n Текст ошибки: {}",
                    id, e.getMessage());
            throw new DatabaseException("Произошла ошибка при обновлении значения элемента с id " + id);
        }
    }

    public static void updateMusicBandByKey(Integer key, MusicBand musicBand) throws DatabaseException {
        String sql = "UPDATE music_band SET (name, coordinates_x, coordinates_y, " +
                "number_of_participants, genre, best_album_name, best_album_sales) = " +
                "(?, ?, ?, ?, ?, ?, ?, ?) WHERE key = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, musicBand.getName());
            pstmt.setInt(2, musicBand.getCoordinates().getX());
            pstmt.setLong(3, musicBand.getCoordinates().getY());
            pstmt.setInt(4, musicBand.getNumberOfParticipants());
            pstmt.setString(5, musicBand.getGenre() != null ? musicBand.getGenre().name() : null);
            pstmt.setString(6, musicBand.getBestAlbum() != null ? musicBand.getBestAlbum().getName() : null);
            if (musicBand.getBestAlbum() != null) pstmt.setDouble(9, musicBand.getBestAlbum().getSales());
            else pstmt.setDouble(7, 0);
            pstmt.setLong(8, key);

            pstmt.executeUpdate();
            logger.info("Значение элемента с ключом {} обновлено на: {}", key, musicBand);
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при обновлении значения элемента с ключом {}\n Текст ошибки: {}",
                    key, e.getMessage());
            throw new DatabaseException("Произошла ошибка при обновлении значения элемента с ключом " + key);
        }
    }

    public static void removeMusicBandByKey(Integer key) throws DatabaseException {
        String sql = "DELETE FROM music_band WHERE key = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, key);

            pstmt.executeUpdate();
            logger.info("Удалён элемент с ключом: {}", key);
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при удалении элемента с ключом {}\n Текст ошибки: {}", key, e.getMessage());
            throw new DatabaseException("Произошла ошибка при удалении элемента с ключом " + key);
        }
    }

    public static void removeMusicBandById(Long id) throws DatabaseException {
        String sql = "DELETE FROM music_band WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, id);

            pstmt.executeUpdate();
            logger.info("Удалён элемент с id: {}", id);
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при удалении элемента с id {}\n Текст ошибки: {}", id, e.getMessage());
            throw new DatabaseException("Произошла ошибка при удалении элемента с id " + id);
        }
    }

    public static void clearCollection() throws DatabaseException {
        String sql = "TRUNCATE TABLE music_band";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.executeUpdate();
            logger.info("Коллекция очищена.");
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при очистке коллекции. Текст ошибки: {}", e.getMessage());
            throw new DatabaseException("Произошла ошибка при очистке коллекции");
        }
    }
}
