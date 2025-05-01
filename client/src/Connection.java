import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Connection {
    public static void main(String[] args) {
        String hostname = "localhost"; // Адрес сервера
        int port = 12345; // Порт, на котором работает сервер

        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Подключение к серверу...");

            // Создаем потоки для чтения и записи
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            // Считываем строку с клавиатуры
            System.out.print("Введите строку: ");
            String userInput = scanner.nextLine();

            // Отправляем строку на сервер
            out.println(userInput);

            // Получаем ответ от сервера
            String response = in.readLine();
            System.out.println("Ответ от сервера: " + response);
        } catch (IOException e) {
            System.err.println("Ошибка при подключении к серверу: " + e.getMessage());
        }
    }
}
