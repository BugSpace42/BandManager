package main.java.utility;

import main.java.connection.responses.CommandResponse;

public interface Responsible {
    CommandResponse formResponse(String[] args);
}
