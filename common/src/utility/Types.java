package utility;

public enum Types {
    INT("int"),
    LONG("long"),
    SHORT("short"),
    DOUBLE("double"),
    BOOLEAN("boolean"),
    STRING("string"),
    MUSIC_BAND("MusicBand"),
    MUSIC_BAND_KEY("MusicBandKey"),
    MUSIC_BAND_ID("MusicBandId"),
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
