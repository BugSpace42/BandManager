package main.java;

import main.java.commands.ServerClear;
import main.java.commands.ServerInsert;
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

        // TODO
        /*
        commandManager.newCommand(new Help());
        commandManager.newCommand(new Info());
        commandManager.newCommand(new Show());
        commandManager.newCommand(new Insert());
        commandManager.newCommand(new Update());
        commandManager.newCommand(new RemoveKey());
        commandManager.newCommand(new Clear());
        commandManager.newCommand(new Save());
        commandManager.newCommand(new ExecuteScript());
        commandManager.newCommand(new Exit());
        commandManager.newCommand(new RemoveGreater());
        commandManager.newCommand(new History());
        commandManager.newCommand(new ReplaceIfGreater());
        commandManager.newCommand(new RemoveAllByNumberOfParticipants());
        commandManager.newCommand(new RemoveAnyByBestAlbum());
        commandManager.newCommand(new PrintFieldDescendingNumberOfParticipants());
         */

        CommandManager.newCommand(new ServerInsert());
        CommandManager.newCommand(new ServerClear());

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