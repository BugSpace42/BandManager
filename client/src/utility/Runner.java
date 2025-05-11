package utility;

import connection.TCPClient;
import connection.requests.CommandRequest;
import connection.responses.CommandResponse;
import exceptions.CanceledCommandException;
import exceptions.TooFewArgumentsException;
import exceptions.TooManyArgumentsException;
import exceptions.UnknownCommandException;
import managers.CollectionManager;
import managers.CommandManager;
import managers.ConsoleManager;
import managers.FileManager;
import utility.entityaskers.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Класс, который управляет работой программы.
 * @author Alina
 */
public class Runner {
    private static Runner runner;
    public static ConsoleManager consoleManager;
    public HashMap<String, Command> commands;
    private boolean running = false;
    private RunningMode currentMode;
    public HashSet<String> scripts = new HashSet<>();
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
    public void start() throws IOException {
        this.commands = client.getCommandMap();
        this.running = true;
        this.currentMode = RunningMode.INTERACTIVE;
        ConsoleManager.greeting();
        ConsoleManager.println(commands);
    }

    /**
     * Спрашивает у пользователя все аргументы команды.
     * @param command команда
     * @return все аргументы, записанные в массив строк
     * @throws CanceledCommandException
     */
    public String[] askArguments(Command command) throws CanceledCommandException {
        String[] askingArguments = command.getArguments();
        String[] arguments = new String[askingArguments.length];
        for (int i = 0; i < askingArguments.length; i++) {
            String type = askingArguments[i];
            arguments[i] = Asker.ask(type);
        }
        return arguments;
    }

    /**
     * Проверяет правильность ввода команды.
     * @param userCommand введённая пользователем команда
     * @return true - если команда введена верно, false или исключение - иначе
     */
    public boolean checkCommand(String[] userCommand)
            throws UnknownCommandException, TooManyArgumentsException, TooFewArgumentsException {
        if (!commands.containsKey(userCommand[0])){
            throw new UnknownCommandException("Не найдена команда " + userCommand[0]);
        }
        if (commands.get(userCommand[0]).getNumberOfArguments() > userCommand.length-1) {
            throw new TooManyArgumentsException("Команда " + userCommand[0] + " должна содержать "
                    + commands.get(userCommand[0]).getNumberOfArguments() + " аргументов");
        }
        if (commands.get(userCommand[0]).getNumberOfArguments() < userCommand.length-1) {
            throw new TooFewArgumentsException("Команда " + userCommand[0] + " должна содержать "
                    + commands.get(userCommand[0]).getNumberOfArguments() + " аргументов");
        }
        return true;
    }

    /**
     * Запускает команду, которую ввёл пользователь.
     * @param userCommand команда
     */
    public void launchCommand(String[] userCommand) {
        try {
            checkCommand(userCommand);
            //int exitCode = commands.get(userCommand[0]).execute(userCommand);
            String[] arguments = askArguments(commands.get(userCommand[0]));

            String[] strings = new String[userCommand.length + arguments.length];
            for (int i = 0; i < userCommand.length; i++) {
                strings[i] = userCommand[i];
            }
            for (int i = 0; i < arguments.length; i++) {
                strings[userCommand.length + i] = arguments[i];
            }

            CommandRequest request = client.formRequest(strings);
            byte[] dataToSend = client.serializeData(request);
            client.sendData(dataToSend);

            byte[] recievedData = client.receiveData();
            CommandResponse commandResponse = client.deserializeCommandData(recievedData);
            // TODO что-то делать с ответом


        } catch (CanceledCommandException e) {
            ConsoleManager.println("Получен сигнал отмены команды.");
        } catch (UnknownCommandException e) {
            if (currentMode == RunningMode.INTERACTIVE) {
                ConsoleManager.printError(e.getMessage());
                ConsoleManager.println("Для получения списка команд введите help.");
            }
        } catch (TooManyArgumentsException | TooFewArgumentsException e) {
            ConsoleManager.printError(e.getMessage());
        } catch (IOException e) {
            ConsoleManager.printError(e.getMessage());
        }
    }

    /**
     * Управляет работой программы.
     */
    public void run() {
        try {
            start();
        } catch (IOException e) {
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
        ConsoleManager.println("конец");
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
