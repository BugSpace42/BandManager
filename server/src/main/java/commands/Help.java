package main.java.commands;

import main.java.utility.ExecutableCommand;
import utility.Report;

public class Help extends ExecutableCommand {
    public Help(){
        super("help", "вывести справку по доступным командам", 0, new String[]{});

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
