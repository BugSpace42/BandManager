package main.java.utility.entityaskers;

import main.java.exceptions.CanceledCommandException;
import main.java.managers.ConsoleManager;

public class PrimitiveAsker {
    public static int askInt() throws CanceledCommandException {
        ConsoleManager.println("Введите число типа int.");
        String str = ConsoleManager.askObject();
        int result;
        try {
            result = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            ConsoleManager.println("Введённая строка не является числом типа int.");
            ConsoleManager.println("Попробуйте снова.");
            result = askInt();
        }
        return result;
    }

    public static long askLong() throws CanceledCommandException {
        ConsoleManager.println("Введите число типа long.");
        String str = ConsoleManager.askObject();
        long result;
        try {
            result = Long.parseLong(str);
        } catch (NumberFormatException e) {
            ConsoleManager.println("Введённая строка не является числом типа long.");
            ConsoleManager.println("Попробуйте снова.");
            result = askLong();
        }
        return result;
    }

    public static double askDouble() throws CanceledCommandException {
        ConsoleManager.println("Введите число типа double.");
        String str = ConsoleManager.askObject();
        double result;
        try {
            result = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            ConsoleManager.println("Введённая строка не является числом типа double.");
            ConsoleManager.println("Попробуйте снова.");
            result = askDouble();
        }
        return result;
    }

    public static short askShort() throws CanceledCommandException {
        ConsoleManager.println("Введите число типа short.");
        String str = ConsoleManager.askObject();
        short result;
        try {
            result = Short.parseShort(str);
        } catch (NumberFormatException e) {
            ConsoleManager.println("Введённая строка не является числом типа short.");
            ConsoleManager.println("Попробуйте снова.");
            result = askShort();
        }
        return result;
    }

    public static boolean askBoolean() throws CanceledCommandException {
        ConsoleManager.println("Введите значение типа boolean.");
        String str = ConsoleManager.askObject();
        boolean result;
        try {
            result = Boolean.parseBoolean(str);
        } catch (NumberFormatException e) {
            ConsoleManager.println("Введённая строка не является числом типа double.");
            ConsoleManager.println("Попробуйте снова.");
            result = askBoolean();
        }
        return result;
    }

    public static String askString() throws CanceledCommandException {
        ConsoleManager.println("Введите строку.");
        String str = ConsoleManager.askObject();
        return str;
    }
}
