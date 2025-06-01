package main.java.managers;

import entity.Album;
import entity.Coordinates;
import entity.MusicBand;
import entity.MusicGenre;
import main.java.connection.AbstractTCPServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;

public class DatabaseManager {
    //private static final String url = "jdbc:postgresql://pg:5432/studs"; // если запускаю сразу на helios
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

    public static HashMap<Integer, MusicBand> getCollection() {
        HashMap<Integer, MusicBand> collection = new HashMap<>();
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
                logger.info("Элемент считан в коллекцию: {}", musicBand.toString());
            }
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при чтении коллекции: {}", e.getMessage());
            return null;
        }
        return collection;
    }

    public static boolean addMusicBand(Integer key, MusicBand musicBand) {
        String sql = "INSERT INTO music_band (key, name, coordinates_x, coordinates_y, creation_date, " +
                "number_of_participants, genre, best_album_name, best_album_sales) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, key);
            pstmt.setString(2, musicBand.getName());
            pstmt.setDouble(3, musicBand.getCoordinates().getX());
            pstmt.setDouble(4, musicBand.getCoordinates().getY());
            pstmt.setTimestamp(5, new java.sql.Timestamp(musicBand.getCreationDate().getTime()));
            pstmt.setInt(6, musicBand.getNumberOfParticipants());
            pstmt.setString(7, musicBand.getGenre() != null ? musicBand.getGenre().name() : null);
            pstmt.setString(8, musicBand.getBestAlbum() != null ? musicBand.getBestAlbum().getName() : null);
            if (musicBand.getBestAlbum() != null) pstmt.setDouble(9, musicBand.getBestAlbum().getSales());
            else pstmt.setDouble(9, 0);

            pstmt.executeUpdate();
            logger.info("Добавлен элемент: {}", musicBand);
            return true;
        } catch (SQLException e) {
            logger.warn("Произошла ошибка при добавлении элемента {}\n Текст ошибки: {}", musicBand, e.getMessage());
            return false;
        }
    }
}
