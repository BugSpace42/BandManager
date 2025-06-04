package main.java.commands;

import entity.Album;
import entity.MusicBand;
import exceptions.DatabaseException;
import exceptions.WrongUserException;
import main.java.managers.CollectionManager;
import commands.ExecutableCommand;
import main.java.managers.DatabaseManager;
import main.java.utility.Commands;
import org.apache.commons.lang3.SerializationUtils;
import utility.ExitCode;
import commands.Report;
import utility.Types;

import java.util.*;

/**
 * Удаляет из коллекции один элемент, значение поля bestAlbum которого эквивалентно заданному.
 * @author Alina
 */
public class RemoveAnyByBestAlbum extends ExecutableCommand {
    public RemoveAnyByBestAlbum() {
        super(Commands.REMOVE_ANY_BY_ALBUM,
                "удалить из коллекции один элемент, значение поля bestAlbum которого эквивалентно заданному",
                new Types[]{}, new Types[]{Types.ALBUM});
    }

    /**
     * Выполняет команду.
     * @param args аргументы команды
     * @return отчёт о выполнении команды
     */
    @Override
    public Report execute(String[] args) {
        Report report;
        try {
            String message;
            Collection<Integer> keysToRemove = new ArrayList<>();
            Album targetAlbum;

            // Декодируем и десериализуем объект Album из аргумента
            byte[] data = Base64.getDecoder().decode(args[1]);
            targetAlbum = SerializationUtils.deserialize(data);

            boolean isRemoved = false;
            HashMap<Integer, MusicBand> collection = CollectionManager.getCollection();
            List<Integer> keys = CollectionManager.getKeyList();
            String owner = args[2];
            ArrayList<Integer> notRemoved = new ArrayList<>();
            int counter = 0;

            for (Integer key : keys) {
                try {
                    if (collection.get(key).equals(targetAlbum)) {
                        counter++;
                        if (!CollectionManager.checkOwner(owner, key)) {
                            notRemoved.add(key);
                            throw new WrongUserException("Невозможно удалить элемент коллекции. " +
                                    "Операцию совершает не владелец элемента. Владелец элемента: " + owner);
                        }
                        DatabaseManager.removeMusicBandByKey(key);
                        CollectionManager.getCollection().remove(key);
                        isRemoved = true;
                        message = "Удалён элемент с ключом " + key;
                        report = new Report(ExitCode.OK.code, null, message);
                        return report;
                    }
                } catch (WrongUserException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Элемент коллекции пропущен.");
                }
            }

            if (counter == 0) {
                message = "Не найдено элементов с заданным полем bestAlbum.";
            } else if (notRemoved.size() > 0) {
                message = "Были найдены элементы с заданным полем bestAlbum с ключами: " + notRemoved.toString();
                message += "\n Не были удалены элементы с ключами: " + notRemoved.toString() + " по причине прав доступа.";
            } else {
                message = "Ни один элемент не был удалён";
            }

            report = new Report(ExitCode.OK.code, null, message);
        } catch (DatabaseException e){
            String errorString = "Ошибка при удалении элемента в базе данных.";
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        } catch (Exception e) {
            String errorString = "Непредвиденная ошибка!";
            System.out.println(e.getMessage());
            report = new Report(ExitCode.ERROR.code, e.getMessage(), errorString);
        }
        return report;
    }
}
