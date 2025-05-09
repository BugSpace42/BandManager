package main.java;

import main.java.connection.TCPServer;
import main.java.managers.CollectionManager;
import main.java.managers.FileManager;

import java.io.*;
import java.net.*;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        String collectionFilePath = "collection.csv";
        Path filePath = Path.of(collectionFilePath);
        FileManager fileManager = new FileManager(filePath.toAbsolutePath());
        try {
            CollectionManager.setCollection(fileManager.readCollection());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            int port = 12345; // Порт, на котором будет работать сервер
            InetAddress addr = InetAddress.getLocalHost(); // Адрес, на котором будет работать сервер
            TCPServer server = new TCPServer(addr, port);
            server.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}