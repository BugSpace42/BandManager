package main.java.managers;

import main.java.connection.SSHPortForwarding;
import main.java.connection.TCPClient;
import main.java.connection.requests.CommandRequest;
import main.java.connection.responses.CommandResponse;
import exceptions.CanceledCommandException;
import exceptions.TooFewArgumentsException;
import exceptions.TooManyArgumentsException;
import exceptions.UnknownCommandException;
import main.java.utility.Command;
import main.java.utility.ExecutableCommand;
import main.java.utility.ExitCode;
import main.java.utility.Report;
import main.java.utility.entityaskers.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Класс, который управляет работой программы.
 * @author Alina
 */
public class Runner {
    private static Runner runner;
    public static ConsoleManager consoleManager;
    public HashMap<String, Command> commands;
    public HashMap<String, ExecutableCommand> clientCommands = new HashMap<>();
    private boolean running = false;
    private RunningMode currentMode;
    public HashSet<String> scripts;
    private TCPClient client;

    /**
     * Перечисление режимов работы программы.
     */
    public enum RunningMode {
        INTERACTIVE,
        SCRIPT
    }

    private Runner() {
        this.consoleManager = ConsoleManager.getConsoleManager();
    }

    /**
     * Метод, использующийся для получения Runner.
     * Создаёт новый объект, если текущий объект ещё не создан.
     * @return runner
     */
    public static Runner getRunner() {
        if (runner == null) {
            runner = new Runner();
        }
        return runner;
    }

    /**
     * Производит действия, необходимые для начала работы.
     * @throws IOException исключение, если невозможно получить информацию о командах от сервера
     */
    public void start() throws IOException, ClassNotFoundException {
        this.commands = client.getCommandMap();
        this.running = true;
        this.currentMode = RunningMode.INTERACTIVE;
        this.scripts = new HashSet<>();
        ConsoleManager.greeting();
    }

    /**
     * Спрашивает у пользователя все аргументы команды.
     * @param command команда
     * @return все аргументы, записанные в массив строк
     * @throws CanceledCommandException
     */
    public ArrayList<byte[]> askArguments(Command command) throws CanceledCommandException {
        String[] askingArguments = command.getArguments();
        ArrayList<byte[]> arguments = new ArrayList<>();
        for (int i = 0; i < askingArguments.length; i++) {
            String type = askingArguments[i];
            arguments.add(Asker.askSerialized(type));
        }
        return arguments;
    }

    /**
     * Проверяет правильность ввода команды.
     * @param userCommand введённая пользователем команда
     * @return true - если команда введена верно, false или исключение - иначе
     */
    public Command checkCommand(String[] userCommand)
            throws UnknownCommandException, TooManyArgumentsException, TooFewArgumentsException {
        Command command;
        if (clientCommands.containsKey(userCommand[0])) {
            command = clientCommands.get(userCommand[0]);
        } else if (commands.containsKey(userCommand[0])) {
            command = commands.get(userCommand[0]);
        } else {
            throw new UnknownCommandException(userCommand[0]);
        }
        if (command.getNumberOfArguments() > userCommand.length-1) {
            throw new TooManyArgumentsException("Команда " + command.getName() + " должна содержать "
                    + command.getNumberOfArguments() + " аргументов");
        }
        if (command.getNumberOfArguments() < userCommand.length-1) {
            throw new TooFewArgumentsException("Команда " + command.getName() + " должна содержать "
                    + command.getNumberOfArguments() + " аргументов");
        }
        return command;
    }

    public CommandResponse executeClientCommand(ExecutableCommand command, String[] args) {
        Report report = command.execute(args);
        CommandResponse commandResponse = new CommandResponse(report.getCode(), report.getError(), report.getMessage());
        return commandResponse;
    }

    /**
     * Запускает команду, которую ввёл пользователь.
     * @param userCommand команда
     */
    public void launchCommand(String[] userCommand) {
        try {
            Command command = checkCommand(userCommand);
            ArrayList<byte[]> arguments = askArguments(command);

            String[] strings = new String[userCommand.length + arguments.size()];
            for (int i = 0; i < userCommand.length; i++) {
                strings[i] = userCommand[i];
            }
            for (int i = 0; i < arguments.size(); i++) {
                byte[] data = arguments.get(i);
                String encodedData = Base64.getEncoder().encodeToString(data);
                strings[userCommand.length + i] = encodedData;
            }

            CommandResponse commandResponse;
            if (clientCommands.containsKey(userCommand[0])) {
                commandResponse = executeClientCommand(clientCommands.get(userCommand[0]), strings);
            }
            else {
                CommandRequest request = client.formRequest(strings);
                byte[] dataToSend = client.serializeData(request);
                client.sendData(dataToSend);
                commandResponse = client.receiveAndDeserialize(CommandResponse.class);
            }
            if (commandResponse.getCode() == ExitCode.OK.code) {
                ConsoleManager.println(commandResponse.getMessage());
                ConsoleManager.println("Команда " + userCommand[0] + " успешно выполнена.");
            } else if (commandResponse.getCode() == ExitCode.CANCEL.code) {
                ConsoleManager.println("Получен сигнал отмены команды.");
            } else if (commandResponse.getCode() == ExitCode.ERROR.code) {
                ConsoleManager.println("При выполнении команды " + userCommand[0] + " произошла ошибка.");
                ConsoleManager.println(commandResponse.getMessage());
            } else if (commandResponse.getCode() == ExitCode.EXIT.code) {
                ConsoleManager.println("Получен сигнал выхода из программы.");
                this.running = false;
            } else {
                ConsoleManager.println("Команда " + userCommand[0] + " не была выполнена.");
            }
        } catch (IOException | ClassNotFoundException e) {
            ConsoleManager.printError("Произошла ошибка при получении ответа от сервера.");
        } catch (CanceledCommandException e) {
            ConsoleManager.println("Получен сигнал отмены команды.");
        } catch (UnknownCommandException e) {
            if (currentMode == RunningMode.INTERACTIVE) {
                ConsoleManager.printError("Не найдена команда " + e.getMessage());
                ConsoleManager.println("Для получения списка команд введите help.");
            }
        } catch (TooManyArgumentsException | TooFewArgumentsException e) {
            ConsoleManager.printError(e.getMessage());
        }
    }

    /**
     * Управляет работой программы.
     */
    public void run() {
        try {
            start();
        } catch (IOException | ClassNotFoundException e) {
            ConsoleManager.printError("Произошла ошибка при получении списка команд от сервера.");
            running = false;
        }

        while(running) {
            String[] currentCommand;
            currentCommand = ConsoleManager.askCommand();
            if (currentCommand != null) {
                launchCommand(currentCommand);
            }
        }
    }

    public void stop() {
        try {
            running = false;
            SSHPortForwarding.disconnect();
            client.close();
            ConsoleManager.println("Программа завершила свою работу.");
        } catch (IOException e) {
            ConsoleManager.printError("Произошла ошибка при завершении работы программы.");
            ConsoleManager.println("Принудительное завершение работы.");
            System.exit(0);
        }
    }

    public void addClientCommand(ExecutableCommand clientCommand) {
        this.clientCommands.put(clientCommand.getName(), clientCommand);
    }

    public boolean getRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public RunningMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(RunningMode currentMode) {
        this.currentMode = currentMode;
    }

    public TCPClient getClient() {
        return client;
    }

    public void setClient(TCPClient client) {
        this.client = client;
    }
}
