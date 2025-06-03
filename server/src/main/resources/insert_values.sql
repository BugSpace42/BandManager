SELECT * FROM music_band;

INSERT INTO music_band (key, name, coordinates_x, coordinates_y, creation_date,
                        number_of_participants, genre, best_album_name, best_album_sales, owner_username)
VALUES (2025, 'sql_name', 123, 123, CURRENT_TIMESTAMP,
        333, 'POP', 'sql_album_name', 444, 'admin');

SELECT * FROM music_band;