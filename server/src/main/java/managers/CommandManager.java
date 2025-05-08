package main.java.managers;

import utility.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CommandManager {
    private static final HashMap<String, Command> commands = new HashMap<>();
    private static final ArrayList<String> commandHistory = new ArrayList<>();

    private CommandManager() {}

    /**
     * Добавляет новую команду.
     * @param command команда
     */
    public static void newCommand(Command command) {
        commands.put(command.getName(), command);
    }

    /**
     * Добавляет команду в историю.
     */
    public static void addToHistory(String command) {
        commandHistory.add(command);
    }

    /**
     * @return словарь с доступными командами
     */
    public static HashMap<String, Command> getCommands() {
        return commands;
    }

    /**
     * @return список выполненных команд
     */
    public static ArrayList<String> getCommandHistory() {
        return commandHistory;
    }

    /**
     * @return список названий доступных команд
     */
    public static ArrayList<String> getCommandList() {
        Set<String> commandSet = commands.keySet();
        ArrayList<String> commandList = new ArrayList<String>();
        for (String cmd : commandSet) {
            commandList.add(cmd);
        }
        return commandList;
    }
}
