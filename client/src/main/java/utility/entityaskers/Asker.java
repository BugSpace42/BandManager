package main.java.utility.entityaskers;

import exceptions.CanceledCommandException;
import org.apache.commons.lang3.SerializationUtils;
import utility.Types;

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

    public static byte[] askSerialized(Types type) throws CanceledCommandException {
        byte[] result = switch (type) {
            case INT -> SerializationUtils.serialize(PrimitiveAsker.askInt());
            case LONG -> SerializationUtils.serialize(PrimitiveAsker.askLong());
            case SHORT -> SerializationUtils.serialize(PrimitiveAsker.askShort());
            case DOUBLE -> SerializationUtils.serialize(PrimitiveAsker.askDouble());
            case BOOLEAN -> SerializationUtils.serialize(PrimitiveAsker.askBoolean());
            case STRING -> SerializationUtils.serialize(PrimitiveAsker.askString());
            case MUSIC_BAND -> SerializationUtils.serialize(MusicBandAsker.askMusicBand());
            case MUSIC_BAND_KEY -> SerializationUtils.serialize(MusicBandAsker.askMusicBandKey());
            case MUSIC_BAND_ID -> SerializationUtils.serialize(MusicBandAsker.askMusicBandId());
            case MUSIC_BAND_NAME -> SerializationUtils.serialize(MusicBandAsker.askMusicBandName());
            case MUSIC_BAND_NUMBER -> SerializationUtils.serialize(MusicBandAsker.askMusicBandName());
            case COORDINATES -> SerializationUtils.serialize(CoordinatesAsker.askCoordinates());
            case ALBUM -> SerializationUtils.serialize(AlbumAsker.askAlbum());
        };
        return result;
    }
}
