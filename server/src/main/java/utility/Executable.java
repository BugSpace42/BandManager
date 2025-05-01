package main.java.utility;

import connection.responses.Response;

/**
 * Интерфейс исполняемых объектов.
 * @author Alina
 */
public interface Executable {
    Response execute(String[] args);
}
