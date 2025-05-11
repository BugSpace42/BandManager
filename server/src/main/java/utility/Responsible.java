package main.java.utility;

import connection.responses.CommandResponse;

public interface Responsible {
    CommandResponse formResponse(String[] args);
}
