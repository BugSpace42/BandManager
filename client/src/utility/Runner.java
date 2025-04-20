package utility;

import exceptions.TooFewArgumentsException;
import exceptions.TooManyArgumentsException;
import exceptions.UnknownCommandException;
import managers.CollectionManager;
import managers.CommandManager;
import managers.ConsoleManager;
import managers.FileManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

/**
 * Класс, который управляет работой программы.
 * @author Alina
 */
public class Runner {
    private static Runner runner;
    public static CommandManager commandManager;
    public static ConsoleManager consoleManager;
    public static CollectionManager collectionManager;
    public static FileManager fileManager;
    public Map<String, Command> commands;
    private boolean running = false;
    private RunningMode currentMode;
    public HashSet<String> scripts = new HashSet<>();;

    /**
     * Перечисление кодов завершения выполнения команды.
     */
    public enum ExitCode {
        OK,
        EXIT,
        CANCEL,
        ERROR
    }

    /**
     * Перечисление режимов работы программы.
     */
    public enum RunningMode {
        INTERACTIVE,
        SCRIPT
    }

    private Runner() {
        this.commandManager = CommandManager.getCommandManager();
        this.consoleManager = ConsoleManager.getConsoleManager();
        this.commands = commandManager.getCommands();
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
     * Устанавливает новое значение поля fileManager.
     * @param fileManager новое значение поля fileManager
     */
    public static void setFileManager(FileManager fileManager) {
        Runner.fileManager = fileManager;
    }

    /**
     * Запускает команду, которую ввёл пользователь.
     * @param userCommand команда
     */
    public void launchCommand(String[] userCommand) {
        try {
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

            ExitCode exitCode = commands.get(userCommand[0]).execute(userCommand);
            if (currentMode == RunningMode.INTERACTIVE) {
                switch (exitCode) {
                    case OK -> {
                        ConsoleManager.println("Команда " + userCommand[0] + " выполнена.");
                        commandManager.addToHistory(userCommand[0]);
                    }
                    case ERROR -> {
                        ConsoleManager.println("При выполнении команды " + userCommand[0] + " произошла ошибка.");
                        ConsoleManager.println("Команда " + userCommand[0] + " не была выполнена.");
                    }
                    case CANCEL -> {
                        ConsoleManager.println("Выполнение команды " + userCommand[0] + " было прервано.");
                        ConsoleManager.println("Команда " + userCommand[0] + " не была выполнена.");
                    }
                    case EXIT -> {
                        ConsoleManager.println("Получена команда выхода из программы.");
                        ConsoleManager.println("Завершение работы программы.");
                        running = false;
                    }
                }
            }
            else {
                switch (exitCode) {
                    case OK -> {
                        ConsoleManager.println("Команда " + userCommand[0] + " выполнена.");
                        commandManager.addToHistory(userCommand[0]);
                    }
                    case ERROR -> {
                        ConsoleManager.println("При выполнении команды " + userCommand[0] + " произошла ошибка.");
                        ConsoleManager.println("Команда " + userCommand[0] + " не была выполнена.");
                    }
                    case CANCEL -> {
                        ConsoleManager.println("Выполнение команды " + userCommand[0] + " было прервано.");
                        ConsoleManager.println("Команда " + userCommand[0] + " не была выполнена.");
                    }
                    case EXIT -> {
                        ConsoleManager.println("Получена команда выхода из программы.");
                        ConsoleManager.println("Завершение работы программы.");
                        running = false;
                    }
                }
            }
        } catch (UnknownCommandException e) {
            if (currentMode == RunningMode.INTERACTIVE) {
                ConsoleManager.printError(e.getMessage());
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
        running = true;
        currentMode = RunningMode.INTERACTIVE;
        if (!fileManager.isFileExist(fileManager.getCollectionFilePath())) {
            ConsoleManager.printError("Не найден файл " + fileManager.getCollectionFilePath());
            ConsoleManager.println("Создана пустая коллекция.");
        }
        else {
            try {
                CollectionManager.setCollection(fileManager.readCollection());
                
            } catch (IOException e) {
                ConsoleManager.printError("Невозможно прочитать коллекцию из файла " + fileManager.getCollectionFilePath());
                ConsoleManager.println("Создана пустая коллекция.");
            }
        }
        collectionManager = CollectionManager.getCollectionManager();
        ConsoleManager.greeting();

        while(running) {
            String[] currentCommand;
            currentCommand = ConsoleManager.askCommand();
            if (currentCommand != null) {
                launchCommand(currentCommand);
            }
        }
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
}
