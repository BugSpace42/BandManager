package main.java.utility.entityaskers;

import main.java.exceptions.AskingArgumentsException;
import main.java.exceptions.CanceledCommandException;
import main.java.managers.Runner;
import org.apache.commons.lang3.SerializationUtils;
import utility.Types;

public class Asker {
    public static byte[] askSerialized(Types type) throws CanceledCommandException, AskingArgumentsException {
        return switch (type) {
            case INT -> SerializationUtils.serialize(PrimitiveAsker.askInt());
            case LONG -> SerializationUtils.serialize(PrimitiveAsker.askLong());
            case SHORT -> SerializationUtils.serialize(PrimitiveAsker.askShort());
            case DOUBLE -> SerializationUtils.serialize(PrimitiveAsker.askDouble());
            case BOOLEAN -> SerializationUtils.serialize(PrimitiveAsker.askBoolean());
            case STRING -> SerializationUtils.serialize(PrimitiveAsker.askString());
            case MUSIC_BAND -> SerializationUtils.serialize(MusicBandAsker.askMusicBand());
            case MUSIC_BAND_KEY -> SerializationUtils.serialize(MusicBandAsker.askMusicBandKey());
            case MUSIC_BAND_KEY_UNIQUE -> {
                Integer key = MusicBandAsker.askMusicBandKey();
                if (Runner.getRunner().getKeyList().contains(key)) {
                    throw new AskingArgumentsException("Полученный ключ уже содержится в коллекции. Ожидался уникальный ключ.");
                }
                yield SerializationUtils.serialize(key);
            }
            case MUSIC_BAND_KEY_CONTAINED -> {
                Integer key = MusicBandAsker.askMusicBandKey();
                if (Runner.getRunner().getKeyList().contains(key)) {
                    yield SerializationUtils.serialize(key);
                }
                throw new AskingArgumentsException("Полученный ключ не содержится в коллекции. Ожидался ключ, содержащийся в коллекции.");
            }
            case MUSIC_BAND_ID -> SerializationUtils.serialize(MusicBandAsker.askMusicBandId());
            case MUSIC_BAND_ID_UNIQUE -> {
                Long id = MusicBandAsker.askMusicBandId();
                if (Runner.getRunner().getIdList().contains(id)) {
                    throw new AskingArgumentsException("Элемент с полученным id уже содержится в коллекции. " +
                            "Ожидался уникальный id.");
                }
                yield SerializationUtils.serialize(id);
            }
            case MUSIC_BAND_ID_CONTAINED -> {
                Long id = MusicBandAsker.askMusicBandId();
                if (Runner.getRunner().getIdList().contains(id)) {
                    yield SerializationUtils.serialize(id);
                }
                throw new AskingArgumentsException("Элемент с полученным id не содержится в коллекции. " +
                        "Ожидался id элемента, который содержится в коллекции.");
            }
            case MUSIC_BAND_NAME -> SerializationUtils.serialize(MusicBandAsker.askMusicBandName());
            case MUSIC_BAND_NUMBER -> SerializationUtils.serialize(MusicBandAsker.askMusicBandNumber());
            case COORDINATES -> SerializationUtils.serialize(CoordinatesAsker.askCoordinates());
            case ALBUM -> SerializationUtils.serialize(AlbumAsker.askAlbum());
        };
    }
}
