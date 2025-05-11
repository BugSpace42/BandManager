package main.java;

import main.java.commands.*;
import main.java.connection.TCPServer;
import main.java.managers.CollectionManager;
import main.java.managers.CommandManager;
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

        CommandManager commandManager = CommandManager.getCommandManager();
        CollectionManager collectionManager = CollectionManager.getCollectionManager();

        CommandManager.newCommand(new Help());
        CommandManager.newCommand(new Info());
        CommandManager.newCommand(new Show());
        CommandManager.newCommand(new Insert());
        CommandManager.newCommand(new Update());
        CommandManager.newCommand(new RemoveKey());
        CommandManager.newCommand(new Clear());
        CommandManager.newCommand(new ExecuteScript());
        CommandManager.newCommand(new Exit());
        CommandManager.newCommand(new RemoveGreater());
        CommandManager.newCommand(new History());
        CommandManager.newCommand(new ReplaceIfGreater());
        CommandManager.newCommand(new RemoveAllByNumberOfParticipants());
        CommandManager.newCommand(new RemoveAnyByBestAlbum());
        CommandManager.newCommand(new PrintFieldDescendingNumberOfParticipants());

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