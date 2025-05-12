package main.java.utility.validators.musicband.bestalbum;

import entity.Album;
import main.java.utility.validators.Validator;

/**
 * Отвечает за правильность данных, введённных пользователем.
 * @author Alina
 */
public class AlbumValidator implements Validator<Album>{
    @Override
    public boolean validate(Album album) {
        return new AlbumNameValidator().validate(album.getName()) 
            && new AlbumSalesValidator().validate(album.getSales());
    }
}
