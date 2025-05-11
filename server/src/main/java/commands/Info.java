package main.java.commands;

import main.java.utility.ExecutableCommand;
import utility.Report;

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
        // Очищаем коллекцию
        Report report = new Report(0, null, "Коллекция не очищена, команды нету");
        return report;
    }
}
