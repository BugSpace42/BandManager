package main.java.commands;

import commands.InsertCommand;
import entity.MusicBand;
import exceptions.CanceledCommandException;

/**
 * Добавляет в коллекцию новый элемент с заданным ключом.
 * @author Alina
 */
public class ServerInsert extends InsertCommand {
    /**
     * Выполняет команду.
     */
    @Override
    public int execute(String[] args){
        // Добавляем элемент
        return 0;
    }
}
