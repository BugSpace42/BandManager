package main.java.managers;

import main.java.connection.requests.CommandRequest;
import main.java.utility.ExecutableCommand;
import main.java.utility.Report;

import java.util.Map;

/**
 * Отвечает за то, чтобы обрабатывать запросы на выполнение команд.
 */
public class CommandRequestManager {
    public static Map<String, ExecutableCommand> commands = CommandManager.getExecutableCommands();

    /**
     * Отдаёт приказ на выполнение команды.
     * @param request запрос на выполнение команды.
     * @return код завершения
     */
    public static Report directCommand(CommandRequest request) {
        String commandName = request.getName();
        return commands.get(commandName).execute(request.getArgs());
    }
}
