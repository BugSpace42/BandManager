package main.java.handlers;

import main.java.connection.AbstractTCPServer;

public class ShutdownHandler extends Thread {

    @Override
    public void run() {
        saveData();
        System.out.println("Завершение работы программы.");
    }

    private void saveData() {
        AbstractTCPServer.saveCollection();
        System.out.println("Данные успешно сохранены.");
    }
}