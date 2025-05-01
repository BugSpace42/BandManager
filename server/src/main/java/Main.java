package main.java;

import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        int port = 12345; // Порт, на котором будет работать сервер

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Сервер запущен и ожидает подключения...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Клиент подключен: " + clientSocket.getInetAddress());

                    // Создаем потоки для чтения и записи
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    // Читаем строку от клиента
                    String inputLine = in.readLine();
                    System.out.println("Получено от клиента: " + inputLine);

                    // Добавляем "!" к строке и отправляем обратно клиенту
                    String response = inputLine + "!";
                    out.println(response);
                } catch (IOException e) {
                    System.err.println("Ошибка при обработке клиента: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при запуске сервера: " + e.getMessage());
        }
    }
}