package utility.entityaskers;

import entity.Album;
import entity.Coordinates;
import entity.MusicBand;
import exceptions.CanceledCommandException;

public class Asker {
    public static String ask(String type) throws CanceledCommandException {
        String result = switch (type) {
            case "int" -> String.valueOf(PrimitiveAsker.askInt());
            case "long" -> String.valueOf(PrimitiveAsker.askLong());
            case "short" -> String.valueOf(PrimitiveAsker.askShort());
            case "double" -> String.valueOf(PrimitiveAsker.askDouble());
            case "boolean" -> String.valueOf(PrimitiveAsker.askBoolean());
            case "string" -> String.valueOf(PrimitiveAsker.askString());
            case "MusicBand" -> String.valueOf(MusicBandAsker.askMusicBand());
            case "MusicBandName" -> String.valueOf(MusicBandAsker.askMusicBandName());
            case "Coordinates" -> String.valueOf(CoordinatesAsker.askCoordinates());
            case "Album" -> String.valueOf(AlbumAsker.askAlbum());
            default -> null;
        };
        return result;
    }
}
