package main.java.managers;

import main.java.utility.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();
    private final ArrayList<String> commandHistory = new ArrayList<>();

    private CommandManager() {}

    /**
     * Добавляет новую команду.
     * @param command команда
     */
    public void newCommand(Command command) {
        commands.put(command.getName(), command);
    }

    /**
     * Добавляет команду в историю.
     */
    public void addToHistory(String command) {
        commandHistory.add(command);
    }

    /**
     * @return словарь с доступными командами
     */
    public HashMap<String, Command> getCommands() {
        return commands;
    }

    /**
     * @return список выполненных команд
     */
    public ArrayList<String> getCommandHistory() {
        return commandHistory;
    }

    /**
     * @return список названий доступных команд
     */
    public ArrayList<String> getCommandList() {
        Set<String> commandSet = commands.keySet();
        ArrayList<String> commandList = new ArrayList<String>();
        for (String cmd : commandSet) {
            commandList.add(cmd);
        }
        return commandList;
    }
}
