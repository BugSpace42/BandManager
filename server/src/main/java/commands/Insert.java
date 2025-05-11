package main.java.commands;

import main.java.utility.ExecutableCommand;
import utility.Report;

/**
 * Добавляет в коллекцию новый элемент с заданным ключом.
 * @author Alina
 */
public class Insert extends ExecutableCommand {
    public Insert() {
        super("insert", "добавить новый элемент с заданным ключом",
                1, new String[]{"MusicBand"});
    }

    /**
     * Выполняет команду.
     * @param args аргументы команды
     * @return отчёт о выполнении команды
     */
    public Report execute(String[] args){
        // Добавляем элемент
        Report report = new Report(0, null, "Элемент не добавлен, команды нету");
        return report;
    }
}
