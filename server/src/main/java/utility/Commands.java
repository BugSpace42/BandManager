package main.java.utility;

import java.util.ArrayList;

public class Commands {
    public static final String CLEAR = "clear";
    public static final String EXIT = "exit";
    public static final String HELP = "help";
    public static final String HISTORY = "history";
    public static final String INFO = "info";
    public static final String INSERT = "insert";
    public static final String PRINT_DESCENDING_NUMBER = "print_field_descending_number_of_participants";
    public static final String REMOVE_BY_NUMBER = "remove_all_by_number_of_participants";
    public static final String REMOVE_ANY_BY_ALBUM = "remove_any_by_best_album";
    public static final String REMOVE_GREATER = "remove_greater";
    public static final String REMOVE_KEY = "remove_key";
    public static final String REPLACE_IF_GREATER = "replace_if_greater";
    public static final String SHOW = "show";
    public static final String UPDATE = "update";

    public static boolean isModifyingCommand(String command) {
        ArrayList<String> modifyingCommands = getModifyingCommands();
        return modifyingCommands.contains(command);
    }

    public static ArrayList<String> getModifyingCommands() {
        ArrayList<String> modifyingCommands = new ArrayList<>();
        modifyingCommands.add(CLEAR);
        modifyingCommands.add(INSERT);
        modifyingCommands.add(REMOVE_BY_NUMBER);
        modifyingCommands.add(REMOVE_ANY_BY_ALBUM);
        modifyingCommands.add(REMOVE_GREATER);
        modifyingCommands.add(REMOVE_KEY);
        modifyingCommands.add(REPLACE_IF_GREATER);
        modifyingCommands.add(UPDATE);
        return modifyingCommands;
    }
}
