package utility;

public enum Types {
    INT("int"),
    LONG("long"),
    SHORT("short"),
    DOUBLE("double"),
    BOOLEAN("boolean"),
    STRING("string"),
    MUSIC_BAND("MusicBand"),
    MUSIC_BAND_KEY("MusicBandKey (Integer)"),
    MUSIC_BAND_KEY_UNIQUE("UniqueMusicBandKey (Integer, not contained in the collection)"),
    MUSIC_BAND_KEY_CONTAINED("ContainedMusicBandKey (Integer, contained in the collection)"),
    MUSIC_BAND_ID("MusicBandId (Long, id > 0)"),
    MUSIC_BAND_ID_UNIQUE("UniqueMusicBandId (Long, id > 0, not contained in the collection)"),
    MUSIC_BAND_ID_CONTAINED("ContainedMusicBandId (Long, id > 0), contained in the collection"),
    MUSIC_BAND_NAME("MusicBandName"),
    MUSIC_BAND_NUMBER("MusicBandNumberOfParticipants"),
    COORDINATES("Coordinates"),
    ALBUM("Album");

    public final String type;
    private Types(String s) {
        type = s;
    }

    public String toString() {
        return type;
    }
}
