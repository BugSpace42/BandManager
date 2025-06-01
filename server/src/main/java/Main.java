package main.java;

import connection.SSHPortForwarding;
import entity.Coordinates;
import entity.MusicBand;
import main.java.commands.*;
import main.java.commands.servercommands.ServerExecuteScript;
import main.java.commands.servercommands.ServerSave;
import main.java.connection.TCPServer;
import main.java.handlers.ShutdownHandler;
import main.java.managers.CollectionManager;
import main.java.managers.CommandManager;
import main.java.managers.DatabaseManager;
import main.java.managers.FileManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.builders.MusicBandBuilder;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.util.HashMap;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("Программа запущена.");

        SSHPortForwarding.connect();
        DatabaseManager.connect();

        CommandManager commandManager = CommandManager.getCommandManager();
        CollectionManager collectionManager = CollectionManager.getCollectionManager();

        CollectionManager.setCollection(DatabaseManager.getCollection());

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
            int port = 53621; // порт, на котором будет работать сервер
            InetAddress addr = InetAddress.getByName("localhost"); // адрес, на котором будет работать сервер
            TCPServer server = new TCPServer(addr, port);
            server.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}