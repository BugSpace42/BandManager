package main.java.commands.servercommands;

import commands.ExecuteScriptCommand;
import utility.ExitCode;
import commands.Report;

/**
 * Считывает и исполняет скрипт из указанного файла.
 * В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
 * Серверная команда, недоступная клиенту.
 * Команда не реализована, будет реализована при необходимости.
 * @author Alina
 */
public class ServerExecuteScript extends ExecuteScriptCommand {
    /**
     * Выполняет команду.
     * @param args аргументы команды
     * @return отчёт о выполнении команды
     */
    @Override
    public Report execute(String[] args){
        // Очищаем коллекцию
        Report report = new Report(ExitCode.ERROR.code, "Скрипт не выполнен (это серверный скрипт)",
                "Скрипт не выполнен (это серверный скрипт)");
        return report;
    }
}
