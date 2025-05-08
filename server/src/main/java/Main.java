package main.java;

import main.java.connection.TCPServer;

import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
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