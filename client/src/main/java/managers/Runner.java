package main.java.managers;

import commands.Command;
import commands.ExecutableCommand;
import commands.Report;
import connection.requests.AuthenticationRequest;
import connection.responses.*;
import exceptions.*;
import connection.SSHPortForwarding;
import main.java.connection.TCPClient;
import connection.requests.CommandRequest;
import main.java.exceptions.AskingArgumentsException;
import main.java.exceptions.CanceledCommandException;
import main.java.exceptions.ServerIsNotAvailableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.*;
import main.java.utility.validators.TypeValidator;
import main.java.utility.entityaskers.*;
import utility.builders.MusicBandBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Класс, который управляет работой программы.
 * @author Alina
 */
public class Runner {
    private static Runner runner;
    public static ConsoleManager consoleManager;
    public static AuthenticationManager authenticationManager;
    public HashMap<String, Command> commands;
    public HashMap<String, ExecutableCommand> clientCommands = new HashMap<>();
    private List<Integer> keyList;
    private List<Long> idList;
    private boolean running = false;
    private boolean isConnected = false;
    private RunningMode currentMode;
    private String[] currentCommand = null;
    public HashSet<String> scripts;
    private TCPClient client;
    private InetSocketAddress addr;

    private Selector selector;
    private SelectionKey keyWrite;
    private SelectionKey keyRead;

    private static final Logger logger = LogManager.getLogger(Runner.class);

    /**
     * Перечисление режимов работы программы.
     */
    public enum RunningMode {
        INTERACTIVE,
        SCRIPT
    }

    private Runner() {}

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

    public void connect() throws IOException {
        selector.select();
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            SocketChannel channel = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (channel.finishConnect()) {
                    // Соединение установлено.
                    // Меняем интересы на чтение и запись
                    key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                }
            }
            keyIterator.remove(); // удаляем обработанный ключ
        }
    }

    /**
     * Проводит аутентификацию пользователя.
     * @throws ServerIsNotAvailableException исключение, если сервер недоступен
     */
    public void authentication() throws ServerIsNotAvailableException {
        boolean authenticated = false;
        while (!authenticated) {
            authenticated = authenticationManager.doAuthentication();
        }
    }

    public void reAuthentication() throws ServerIsNotAvailableException {
        AuthenticationRequest request = new AuthenticationRequest(AuthenticationCommands.LOGIN,
                authenticationManager.getLogin(), authenticationManager.getPassword());
        byte[] data = client.serializeData(request);
        sendData(data);
    }

    /**
     * Производит действия, необходимые для начала работы.
     * @throws IOException исключение, если невозможно получить информацию о командах от сервера
     */
    public void start() throws IOException, ClassNotFoundException, ServerIsNotAvailableException {
        connect();
        if (isConnected) {
            reAuthentication();
        }
        else {
            authentication();
            this.isConnected = true;
        }

        this.addr = client.getSocketAddress();

        StartInfoResponse startInfoResponse = client.getResponse();
        this.commands = startInfoResponse.getCommandMapResponse().getCommandMap();
        this.keyList = startInfoResponse.getKeyList().getKeyList();
        this.idList = startInfoResponse.getIdList().getIdList();


        // this.commands = client.getCommandMap();
        // this.keyList = client.getKeyList();
        // this.idList = client.getIdList();

        Long maxId = 0L;
        for (Long id : idList) {
            if (id > maxId) {
                maxId = id;
            }
        }
        MusicBandBuilder.setCurrentId(maxId + 1);

        this.running = true;
        this.currentMode = RunningMode.INTERACTIVE;
        this.scripts = new HashSet<>();
        ConsoleManager.println("Выполнены действия по подготовке приложения к работе.\n");
        ConsoleManager.greeting();
    }

    /**
     * Спрашивает у пользователя все аргументы команды.
     * @param command команда
     * @return все аргументы, записанные в массив строк
     */
    public ArrayList<byte[]> askArguments(Command command) throws CanceledCommandException, AskingArgumentsException {
        Types[] askingArguments = command.getArguments();
        ArrayList<byte[]> arguments = new ArrayList<>();
        for (int i = 0; i < askingArguments.length; i++) {
            Types type = askingArguments[i];
            arguments.add(Asker.askSerialized(type));
        }
        return arguments;
    }

    /**
     * Проверяет правильность введённых позиционных аргументов.
     * @param command команда
     * @param userCommand введённая пользователем команда с аргументами
     * @return true - если все аргументы верны, false или исключение - иначе
     */
    public boolean checkPositionalArguments(Command command, String[] userCommand) throws AskingArgumentsException {
        try {
            Types[] types = command.getPositionalArguments();
            for (int i = 0; i < types.length; i++) {
                Types type = types[i];
                String value = userCommand[i + 1];
                if (!TypeValidator.isTypeValid(type, value)) {
                    return false;
                }
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Проверяет правильность ввода команды.
     * @param userCommand введённая пользователем команда
     * @return true - если команда введена верно, false или исключение - иначе
     */
    public Command checkCommand(String[] userCommand)
            throws UnknownCommandException, TooManyArgumentsException, TooFewArgumentsException,
            WrongValueException, AskingArgumentsException, CanceledCommandException {
        Command command;
        if (clientCommands.containsKey(userCommand[0])) {
            command = clientCommands.get(userCommand[0]);
        } else if (commands.containsKey(userCommand[0])) {
            command = commands.get(userCommand[0]);
        } else {
            throw new UnknownCommandException(userCommand[0]);
        }
        if (command.getNumberOfPositionalArguments() > userCommand.length-1) {
            throw new TooManyArgumentsException("Команда " + command.getName() + " должна содержать "
                    + command.getNumberOfPositionalArguments() + " аргументов");
        }
        if (command.getNumberOfPositionalArguments() < userCommand.length-1) {
            throw new TooFewArgumentsException("Команда " + command.getName() + " должна содержать "
                    + command.getNumberOfPositionalArguments() + " аргументов");
        }
        if (! checkPositionalArguments(command, userCommand)) {
            String message = "Введены некорректные аргументы. Команда " + command.getName() +
                    " должна получать аргументы следующих типов: \n";
            for (Types type : command.getPositionalArguments()) {
                message += type.toString() + " ";
            }
            throw new WrongValueException(message);
        }
        return command;
    }

    public CompositeResponse executeClientCommand(ExecutableCommand command, String[] args) {
        Report report = command.execute(args);
        CommandResponse commandResponse = new CommandResponse(report.getCode(), report.getError(), report.getMessage());
        KeyListResponse keyListResponse = new KeyListResponse(keyList);
        IdListResponse idListResponse = new IdListResponse(idList);
        return new CompositeResponse(commandResponse, keyListResponse, idListResponse);
    }

    public void analyzeCommandResponse(CommandResponse commandResponse, String[] userCommand) {
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
    }

    public String[] combineCommandResponseStrings(String[] userCommand, ArrayList<byte[]> arguments) {
        String[] strings = new String[userCommand.length + arguments.size()];
        for (int i = 0; i < userCommand.length; i++) {
            strings[i] = userCommand[i];
        }
        for (int i = 0; i < arguments.size(); i++) {
            byte[] data = arguments.get(i);
            String encodedData = Base64.getEncoder().encodeToString(data);
            strings[userCommand.length + i] = encodedData;
        }
        return strings;
    }

    public void sendCommandData(String[] strings)
            throws IOException, ClassNotFoundException, ServerIsNotAvailableException {
        CommandRequest request = client.formCommandRequest(strings);
        byte[] dataToSend = client.serializeData(request);
        sendData(dataToSend);
    }

    public <T> T readData() throws IOException, ClassNotFoundException {
        // Ждем, пока канал не станет готов к чтению
        while (true) {
            int readyChannels = selector.select(); // блокирует до события
            if (readyChannels == 0) continue; // если ничего не готово, повторяем

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey currentKey = keyIterator.next();
                if (currentKey.isReadable()) {
                    // Канал готов к чтению
                    T object = client.receiveAndDeserialize((SocketChannel) currentKey.channel());
                    keyIterator.remove(); // удаляем обработанный ключ
                    return object;
                }
                keyIterator.remove(); // удаляем обработанный ключ
            }
        }
    }

    /**
     * Отправляет данные на сервер.
     * @param data данные для отправки
     * @throws ServerIsNotAvailableException исключение, которое выбрасывается, если сервер временно недоступен
     */
    public void sendData(byte[] data) throws ServerIsNotAvailableException {
        try {
            client.prepareData(data);
            while (client.isSending()) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey currentKey = iterator.next();
                    iterator.remove(); // Удаляем обработанный ключ
                    if (currentKey.isValid()) {
                        if (currentKey.isWritable()) {
                            client.sendData(currentKey);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ServerIsNotAvailableException("При отправлении данных на сервер произошла ошибка.\n" +
                    "Сервер временно недоступен.");
        }
    }

    /**
     * Пытается восстановить подключение к серверу.
     */
    public void resetConnection() {
        ConsoleManager.println("Производится повторное подключение к серверу.");
        boolean isConnected = false;
        while (!isConnected) {
            try {
                Selector newSelector = Selector.open();
                TCPClient newClient = new TCPClient(addr.getAddress(), addr.getPort(), newSelector);
                setClient(newClient);
                setSelector(newSelector);
                start();
                isConnected = true;
                ConsoleManager.println("Подключение к серверу восстановлено.");
            } catch (IOException | ClassNotFoundException e) {
                ConsoleManager.printError("Ошибка при переподключении к серверу.");
                ConsoleManager.println("Закрытие приложения.");
                stop();
            } catch (ServerIsNotAvailableException e) {
                ConsoleManager.printError("Ошибка при переподключении к серверу.");
                ConsoleManager.println("Повторное подключение.");
                resetConnection();
            }
        }
    }

    /**
     * Запускает команду, которую ввёл пользователь.
     * @param userCommand команда
     */
    public void launchCommand(String[] userCommand) throws ServerIsNotAvailableException {
        try {
            Command command = checkCommand(userCommand);
            ArrayList<byte[]> arguments = askArguments(command);
            String[] strings = combineCommandResponseStrings(userCommand, arguments);

            CompositeResponse compositeResponse;
            if (clientCommands.containsKey(userCommand[0])) {
                compositeResponse = executeClientCommand(clientCommands.get(userCommand[0]), strings);
            }
            else {
                sendCommandData(strings);
                compositeResponse = client.getCompositeResponse();
            }

            CommandResponse commandResponse = compositeResponse.getCommandResponse();
            KeyListResponse keyListResponse = compositeResponse.getKeyList();
            IdListResponse idListResponse = compositeResponse.getIdList();

            analyzeCommandResponse(commandResponse, userCommand);
            this.keyList = keyListResponse.getKeyList();
            this.idList = idListResponse.getIdList();
        } catch (IOException e) {
            throw new ServerIsNotAvailableException("Произошла ошибка при получении ответа от сервера.\nСервер временно недоступен.");
        } catch (ClassNotFoundException e) {
            ConsoleManager.printError("Произошла ошибка при получении ответа от сервера.");
        } catch (CanceledCommandException e) {
            ConsoleManager.println("Получен сигнал отмены команды.");
            currentCommand = null;
        } catch (UnknownCommandException e) {
            if (currentMode == RunningMode.INTERACTIVE) {
                ConsoleManager.printError("Не найдена команда " + e.getMessage());
                ConsoleManager.println("Для получения списка команд введите help.");
            }
        } catch (TooManyArgumentsException | TooFewArgumentsException | WrongValueException e) {
            ConsoleManager.printError(e.getMessage());
        } catch (AskingArgumentsException e) {
            ConsoleManager.printError("Во время чтения аргумента произошла ошибка:\n" + e.getMessage());
            ConsoleManager.println("Команда не будет выполнена.");
        }
    }

    /**
     * Управляет работой программы.
     */
    public void run() {
        Runner.consoleManager = ConsoleManager.getConsoleManager();
        Runner.authenticationManager = new AuthenticationManager(this);
        try {
            start();
        } catch (IOException | ClassNotFoundException | ServerIsNotAvailableException e) {
            logger.error("Произошла ошибка во время начала работы с сервером.", e);
            ConsoleManager.printError("Произошла ошибка во время начала работы с сервером.");
            ConsoleManager.printError("Завершение работы приложения.");
            stop();
        }

        while(running) {
            try {
                currentCommand = ConsoleManager.askCommand();
                if (currentCommand != null) {
                    launchCommand(currentCommand);
                }
                else {
                    ConsoleManager.println("Поток ввода закрыт.");
                    stop();
                }
            } catch (ServerIsNotAvailableException e) {
                ConsoleManager.printError(e.getMessage());
                resetConnection();
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

    public List<Integer> getKeyList() {
        return keyList;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public TCPClient getClient() {
        return client;
    }

    public void setClient(TCPClient client) {
        this.client = client;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }
}
