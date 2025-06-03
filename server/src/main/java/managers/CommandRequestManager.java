package main.java.managers;

import connection.requests.CommandRequest;
import commands.ExecutableCommand;
import commands.Report;
import main.java.utility.Commands;

import java.util.Map;

/**
 * Отвечает за то, чтобы обрабатывать запросы на выполнение команд.
 */
public class CommandRequestManager {
    public static Map<String, ExecutableCommand> commands = CommandManager.getExecutableCommands();
    public static Map<String, ExecutableCommand> serverCommands = CommandManager.getServerCommands();

    /**
     * Отдаёт приказ на выполнение команды.
     * @param request запрос на выполнение команды.
     * @return отчёт о выполнении команды
     */
    public static Report directCommand(CommandRequest request) {
        String commandName = request.getName();
        String username = request.getUsername();
        if (Commands.isModifyingCommand(commandName)) {
            String[] args = request.getArgs();
            String[] newArgs = new String[args.length + 1];
            System.arraycopy(args, 0, newArgs, 0, args.length);
            newArgs[args.length] = username;
            return commands.get(commandName).execute(newArgs);
        }
        return commands.get(commandName).execute(request.getArgs());
    }

    /**
     * Отдаёт приказ на выполнение серверной команды.
     * @param request запрос на выполнение команды.
     * @return отчёт о выполнении команды
     */
    public static Report directServerCommand(CommandRequest request) {
        String commandName = request.getName();
        return serverCommands.get(commandName).execute(request.getArgs());
    }
}
