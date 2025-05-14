package main.java;

import main.java.commands.*;
import main.java.commands.servercommands.ServerExecuteScript;
import main.java.commands.servercommands.ServerSave;
import main.java.connection.TCPServer;
import main.java.handlers.ShutdownHandler;
import main.java.managers.CollectionManager;
import main.java.managers.CommandManager;
import main.java.managers.FileManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;
import java.nio.file.Path;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("Программа запущена.");
        String collectionFilePath = "res/collection.csv"; // путь к файлу с коллекцией
        Path filePath = Path.of(collectionFilePath);
        FileManager fileManager = FileManager.getFileManager(filePath.toAbsolutePath());
        try {
            CollectionManager.setCollection(fileManager.readCollection());
        } catch (IOException e) {
            logger.warn("Ошибка при чтении коллекции из файла{}", fileManager.getCollectionFilePath(), e);
            logger.info("Создана пустая коллекция.");
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
        CommandManager.newCommand(new ServerExecuteScript());
        CommandManager.newCommand(new Exit());
        CommandManager.newCommand(new RemoveGreater());
        CommandManager.newCommand(new History());
        CommandManager.newCommand(new ReplaceIfGreater());
        CommandManager.newCommand(new RemoveAllByNumberOfParticipants());
        CommandManager.newCommand(new RemoveAnyByBestAlbum());
        CommandManager.newCommand(new PrintFieldDescendingNumberOfParticipants());

        CommandManager.newServerCommand(new ServerSave());

        ShutdownHandler handler = new ShutdownHandler();
        Runtime.getRuntime().addShutdownHook(new Thread(handler));

        try {
            int port = 12345; // порт, на котором будет работать сервер
            InetAddress addr = InetAddress.getByName("localhost"); // адрес, на котором будет работать сервер
            TCPServer server = new TCPServer(addr, port);
            server.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}