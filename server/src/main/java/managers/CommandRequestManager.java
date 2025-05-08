package main.java.managers;

import connection.requests.CommandRequest;
import utility.Command;

import java.util.Map;

/**
 * Отвечает за то, чтобы обрабатывать запросы на выполнение команд.
 */
public class CommandRequestManager {
    public static Map<String, Command> commands = CommandManager.getCommands();

    /**
     * Отдаёт приказ на выполнение команды.
     * @param request запрос на выполнение команды.
     * @return код завершения
     */
    public static int directCommand(CommandRequest request) {
        String commandName = request.getName();
        int exitCode = commands.get(commandName).execute(request.getArgs());
        return exitCode;
    }
}
