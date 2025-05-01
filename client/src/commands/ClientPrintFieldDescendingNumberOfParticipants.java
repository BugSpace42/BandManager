package commands;

import entity.MusicBand;
import managers.CollectionManager;
import managers.ConsoleManager;
import utility.ExitCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Выводит значения поля numberOfParticipants всех элементов в порядке убывания.
 * @author Alina
 */
public class ClientPrintFieldDescendingNumberOfParticipants extends PrintFieldDescendingNumberOfParticipantsCommand {
    /**
     * Выполняет команду.
     */
    @Override
    public int execute(String[] args){
        CollectionManager collectionManager = CollectionManager.getCollectionManager();
        try {
            if (collectionManager.getCollection().isEmpty()) {
                ConsoleManager.println("Коллекция пуста.");
                return ExitCode.OK.code;
            }

            ArrayList<Integer> numberOfParticipantsList = new ArrayList<>();
        
            for (Map.Entry<Integer, MusicBand> entry : collectionManager.getCollection().entrySet()) {
                numberOfParticipantsList.add(entry.getValue().getNumberOfParticipants());
            }

            Collections.sort(numberOfParticipantsList, Collections.reverseOrder());
            ConsoleManager.println("Значения поля numberOfParticipants всех элементов в порядке убывания:");
            for (Integer number : numberOfParticipantsList) {
                ConsoleManager.print(number + " ");
            }
            ConsoleManager.print("\n");
            return ExitCode.OK.code;
        } catch (Exception e) {
            ConsoleManager.printError("Непредвиденная ошибка!");
            return ExitCode.ERROR.code;
        }
    }
}
