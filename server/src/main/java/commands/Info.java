package main.java.commands;

import entity.MusicBand;
import main.java.managers.CollectionManager;
import commands.ExecutableCommand;
import main.java.utility.Commands;
import utility.ExitCode;
import commands.Report;
import utility.Types;

import java.util.HashMap;

/**
 * Выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
 * @author Alina
 */
public class Info extends ExecutableCommand {
    public Info(){
        super(Commands.INFO, "вывести информацию о коллекции",
                new Types[]{}, new Types[]{});
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
            infoString += " Количество элементов: " + collection.size();

            report = new Report(ExitCode.OK.code, null, infoString);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;

    }
}
