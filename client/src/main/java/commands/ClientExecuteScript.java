package main.java.commands;

import commands.ExecuteScriptCommand;
import exceptions.ScriptRecursionException;
import main.java.managers.ConsoleManager;
import main.java.managers.Runner;
import utility.ExitCode;
import utility.Report;

import java.io.*;
import java.util.Scanner;

/**
 * Считывает и исполняет скрипт из указанного файла.
 * В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
 * @author Alina
 */
public class ClientExecuteScript extends ExecuteScriptCommand {
    public Report execute(String[] args) {
        Report report;
        String message;
        Runner runner = Runner.getRunner();
        String scriptName = args[1];
        Runner.RunningMode previousMode = runner.getCurrentMode();
        try {
            if (runner.scripts.contains(scriptName)) {
                throw new ScriptRecursionException("Скрипт " + scriptName + " уже выполняется.");
            }
            runner.scripts.add(scriptName);
            runner.setCurrentMode(Runner.RunningMode.SCRIPT);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(scriptName));
            Scanner oldScanner = ConsoleManager.getScanner();
            Scanner newScanner = new Scanner(reader);
            ConsoleManager.setScanner(newScanner);
            while (ConsoleManager.getScanner().hasNext()) {
                String[] currentScriptCommand = ConsoleManager.readCommand();
                runner.launchCommand(currentScriptCommand);
                if (!runner.getRunning()) {
                    report = new Report(ExitCode.EXIT.code, null, null);
                    return report;
                }
            }
            ConsoleManager.setScanner(oldScanner);
            runner.scripts.remove(scriptName);
            runner.setCurrentMode(previousMode);
            message = "Скрипт из файла " + scriptName + " успешно выполнен.";
            report = new Report(ExitCode.OK.code, null, message);
        } catch (FileNotFoundException e) {
            String errorString = "Файл " + scriptName + " не найден.";
            try {
                ConsoleManager.println("Текущий репозиторий: " + new java.io.File(".").getCanonicalPath());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            runner.scripts.remove(scriptName);
            runner.setCurrentMode(previousMode);
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        } catch (ScriptRecursionException e) {
            String errorString = "Скрипт " + scriptName + " уже выполняется.";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        } catch (IOException e) {
            String errorString = "Невозможно прочитать скрипт из файла " + scriptName;
            runner.scripts.remove(scriptName);
            runner.setCurrentMode(previousMode);
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
