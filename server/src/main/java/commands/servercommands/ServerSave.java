package main.java.commands.servercommands;

import main.java.utility.ExecutableCommand;
import main.java.utility.Report;

/**
 * Сохраняет коллекцию в файл.
 * Серверная команда, недоступная клиенту.
 * @author Alina
 */
public class ServerSave extends ExecutableCommand {
    public ServerSave() {
        super("save", "сохранить коллекцию в файл",
                0, new String[]{});
    }

    /**
     * Выполняет команду.
     * @param args аргументы команды
     * @return отчёт о выполнении команды
     */
    @Override
    public Report execute(String[] args){
        // Сохраняем коллекцию
        Report report = new Report(0, null, "Коллекция не сохранена, команды нету");
        return report;
    }
}
