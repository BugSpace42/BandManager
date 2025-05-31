package main.java.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String url = "jdbc:postgresql://pg:5432/studs";
    private static final String user = "s409478";
    private static final String password = "KLJNrXjJbVh9iVTm";

    public static void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Подключение успешно!");
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер PostgreSQL не найден");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных");
            e.printStackTrace();
        }
    }
}
