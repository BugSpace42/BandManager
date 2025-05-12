package main.java.utility;

/**
 * Перечисление кодов завершения выполнения команды.
 */
public enum ExitCode {
     OK(0),
     EXIT(1),
     CANCEL(2),
     ERROR(3);

    public final int code;

    private ExitCode(final int exitCode) {
        code = exitCode;
    }

    public static ExitCode valueOf(final int exitCode) {
        for (ExitCode value : ExitCode.values()) {
            if (value.code == exitCode) {
                return value;
            }
        }
        return null;
    }
}
