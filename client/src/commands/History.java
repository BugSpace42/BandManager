/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package commands;

import managers.ConsoleManager;
import utility.Command;
import utility.Runner;
import utility.Runner.ExitCode;

import java.util.ArrayList;

/**
 * Выводит последние 8 команд (без их аргументов)
 * @author Alina
 */
public class History extends Command{

    public History() {
        super("history", "вывести последние 8 команд", 0);
    }
    
    /**
     * Выполняет команду.
     */
    @Override
    public ExitCode execute(String[] args){
        Runner runner = Runner.getRunner();
        try {
            int numberOfCommands = 8;
            ArrayList<String> history = runner.commandManager.getCommandHistory();
            if (history.isEmpty()) {
                ConsoleManager.println("История команд пуста.");
                return Runner.ExitCode.OK;
            }
            ConsoleManager.println("История команд (начиная с последней):");
            for (int i = 0; i < Math.min(numberOfCommands, history.size()); i++) {
                ConsoleManager.println(history.get(history.size()-i-1));
            }
            return Runner.ExitCode.OK;
        } catch (Exception e) {
            ConsoleManager.printError("Непредвиденная ошибка!");
            return ExitCode.ERROR;
        }
    }
}
