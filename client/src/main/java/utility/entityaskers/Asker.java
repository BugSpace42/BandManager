package main.java.utility.entityaskers;

import exceptions.CanceledCommandException;
import org.apache.commons.lang3.SerializationUtils;

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

    public static byte[] askSerialized(String type) throws CanceledCommandException {
        byte[] result = switch (type) {
            case "int" -> SerializationUtils.serialize(Integer.valueOf(PrimitiveAsker.askInt()));
            case "long" -> SerializationUtils.serialize(Long.valueOf(PrimitiveAsker.askLong()));
            case "short" -> SerializationUtils.serialize(Short.valueOf(PrimitiveAsker.askShort()));
            case "double" -> SerializationUtils.serialize(Double.valueOf(PrimitiveAsker.askDouble()));
            case "boolean" -> SerializationUtils.serialize(Boolean.valueOf(PrimitiveAsker.askBoolean()));
            case "string" -> SerializationUtils.serialize(PrimitiveAsker.askString());
            case "MusicBand" -> SerializationUtils.serialize(MusicBandAsker.askMusicBand());
            case "MusicBandName" -> SerializationUtils.serialize(MusicBandAsker.askMusicBandName());
            case "Coordinates" -> SerializationUtils.serialize(CoordinatesAsker.askCoordinates());
            case "Album" -> SerializationUtils.serialize(AlbumAsker.askAlbum());
            default -> null;
        };
        return result;
    }
}
