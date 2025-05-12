package main.java.commands;

import main.java.utility.ExecutableCommand;

public abstract class ExecuteScriptCommand extends ExecutableCommand {
    public ExecuteScriptCommand() {
        super("execute_script", "считать и исполнить скрипт из указанного файла",
                1, new String[]{});
    }
}
