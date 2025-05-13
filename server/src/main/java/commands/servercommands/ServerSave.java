package main.java.commands.servercommands;

import main.java.managers.CollectionManager;
import main.java.managers.FileManager;
import main.java.utility.ExecutableCommand;
import main.java.utility.ExitCode;
import main.java.utility.Report;

import java.io.IOException;

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
        Report report;
        try {
            FileManager fileManager = FileManager.getFileManager();
            if (fileManager != null) {
                fileManager.writeCollection(CollectionManager.getCollection());
                String message = "Коллекция сохранена в файл " + fileManager.getCollectionFilePath();
                report = new Report(ExitCode.OK.code, null, message);
            }
            else {
                String errorString = "FileManager не инициализирован, сохранить коллекцию невозможно.";
                report = new Report(ExitCode.ERROR.code, errorString, errorString);
            }
        } catch (IOException e) {
            String errorString = "Невозможно записать коллекцию в файл";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
