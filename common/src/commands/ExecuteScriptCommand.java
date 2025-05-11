package commands;

import utility.Command;

public abstract class ExecuteScriptCommand extends Command {
    public ExecuteScriptCommand(){
        super("execute_script", "считать и исполнить скрипт из указанного файла",
                1, new String[]{"String"});
    }
}