DROP TABLE IF EXISTS music_band;
DROP SEQUENCE IF EXISTS music_band_id_seq;

CREATE SEQUENCE IF NOT EXISTS music_band_id_seq;
CREATE TABLE IF NOT EXISTS music_band (
    key INT NOT NULL,
    id BIGINT PRIMARY KEY DEFAULT nextval('music_band_id_seq'),
    name VARCHAR(255) NOT NULL,
    coordinates_x INT NOT NULL,
    coordinates_y BIGINT NOT NULL,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    number_of_participants INTEGER NOT NULL CHECK (number_of_participants > 0),
    genre VARCHAR(50),
    best_album_name VARCHAR(255),
    best_album_sales DOUBLE PRECISION DEFAULT NULL
    );

SELECT * FROM music_band;