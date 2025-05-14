package main.java.commands;

import entity.MusicBand;
import main.java.managers.CollectionManager;
import utility.ExecutableCommand;
import utility.ExitCode;
import utility.Report;

import java.util.HashMap;

/**
 * Выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
 * @author Alina
 */
public class Info extends ExecutableCommand {
    public Info(){
        super("info", "вывести информацию о коллекции", 0, new String[]{});
    }

    /**
     * Выполняет команду.
     * @param args аргументы команды
     * @return отчёт о выполнении команды
     */
    @Override
    public Report execute(String[] args){
        Report report = null;
        try {
            CollectionManager collectionManager = CollectionManager.getCollectionManager();
            HashMap<Integer, MusicBand> collection = CollectionManager.getCollection();
            String infoString = "Информация о коллекции:\n";
            infoString += " Тип коллекции: " + collection.getClass().getName() + "\n";
            infoString += " Дата инициализации коллекции: " + collectionManager.getInitDate() + "\n";
            infoString += " Количество элементов: " + collection.size() + "\n";

            report = new Report(ExitCode.OK.code, null, infoString);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;

    }
}
