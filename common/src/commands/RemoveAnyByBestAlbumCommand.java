package commands;

import utility.Command;

public abstract class RemoveAnyByBestAlbumCommand extends Command {
    public RemoveAnyByBestAlbumCommand() {
        super("remove_any_by_best_album",
                "удалить из коллекции один элемент, значение поля bestAlbum которого эквивалентно заданному", 0);
    }
}
