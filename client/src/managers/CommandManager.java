package managers;

import utility.Command;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс, который управляет командами.
 * @author Alina
 */
public class CommandManager {
    private static CommandManager commandManager;
    private final HashMap<String, Command> commands = new HashMap<>();
    private final ArrayList<String> commandHistory = new ArrayList<>();

    private CommandManager() {}

    /**
     * Метод, использующийся для получения CommandManager.
     * Создаёт новый объект, если текущий объект ещё не создан.
     * @return commandManager
     */
    public static CommandManager getCommandManager() {
        if (commandManager == null) {
            commandManager = new CommandManager();
        }
        return commandManager;
    }
}
