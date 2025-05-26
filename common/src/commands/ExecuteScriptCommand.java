package commands;

import utility.Types;

public abstract class ExecuteScriptCommand extends ExecutableCommand {
    public ExecuteScriptCommand() {
        super("execute_script", "считать и исполнить скрипт из указанного файла",
                new Types[]{Types.STRING}, new Types[]{});
    }
}
