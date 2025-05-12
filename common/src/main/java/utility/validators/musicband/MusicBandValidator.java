package main.java.utility.validators.musicband;

import entity.MusicBand;
import main.java.utility.validators.Validator;
import main.java.utility.validators.musicband.bestalbum.AlbumValidator;
import main.java.utility.validators.musicband.coordinates.CoordinatesValidator;

/**
 * Отвечает за правильность данных, введённых пользователем.
 * @author Alina
 */
public class MusicBandValidator implements Validator<MusicBand>{
    @Override
    public boolean validate(MusicBand musicBand) {
        return new IdValidator().validate(musicBand.getId())
            && new NameValidator().validate(musicBand.getName())
            && new CoordinatesValidator().validate(musicBand.getCoordinates())
            && new CreationDateValidator().validate(musicBand.getCreationDate())
            && new NumberOfParticipantsValidator().validate(musicBand.getNumberOfParticipants())
            && new AlbumValidator().validate(musicBand.getBestAlbum());
    }
}
