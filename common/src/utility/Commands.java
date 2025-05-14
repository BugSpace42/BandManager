package utility;

public enum Commands {
    HELP("help"),
    SHOW("show"),
    INSERT("insert"),
    UPDATE("update"),
    REMOVE_KEY("remove_key"),
    CLEAR("clear"),
    SAVE("save"),
    EXECUTE_SCRIPT("execute_script"),
    EXIT("exit"),
    REMOVE_GREATER("remove_greater"),
    HISTORY("history"),
    REPLACE_IF_GREATER("replace_if_greater"),
    REMOVE_ALL_BY_NUMBER_OF_PARTICIPANTS("remove_all_by_number_of_participants"),
    REMOVE_ANY_BY_BEST_ALBUM("remove_any_by_best_album"),
    PRINT_FIELD_DESCENDING_NUMBER_OF_PARTICIPANTS("print_field_descending_number_of_participants");

    private final String name;

    Commands(String name) {
        this.name = name;
    }
}
