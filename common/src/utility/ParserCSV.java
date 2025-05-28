package utility;

import entity.*;
import exceptions.IdExistsException;
import exceptions.WrongValueException;
import org.apache.commons.csv.*;
import utility.validators.*;
import utility.validators.musicband.*;
import utility.validators.musicband.bestalbum.*;
import utility.validators.musicband.coordinates.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Преобразует коллекцию объектов класса MusicBand в файл формата csv, и наоборот. 
 * @author Alina
 */
public class ParserCSV {
    /**
     * Получает коллекцию объектов класса MusicBand из файла формата csv.
     * @param fileLines строки файла в формате csv
     * @return полученная коллекция
     */
    public static HashMap<Integer, MusicBand> parseCollectionFromCSV(List<String> fileLines) throws IOException {
        HashMap<Integer, MusicBand> collection = new HashMap<>();
        List<Integer> keyList = new ArrayList<>();
        List<Long> idList = new ArrayList<>();

        if (fileLines.size() < 2) {
            return collection;
        }

        String headerLine = fileLines.get(0);

        String csvContent = String.join("\n", fileLines);
        CSVParser parser = CSVFormat.DEFAULT
                .withNullString("")
                .withFirstRecordAsHeader()
                .parse(new java.io.StringReader(csvContent));

        List<CSVRecord> records = parser.getRecords();
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            try {
                // Получение значений по индексу и валидация
                Integer key;
                if (SpecificTypeValidator.isInteger(record.get(0))) {
                    key = Integer.parseInt(record.get(0));
                    if (keyList.contains(key)) {
                        throw new IdExistsException("Музыкальная группа с ключом" + key + " уже существует.");
                    }
                    keyList.add(key);
                }
                else {
                    throw new WrongValueException("В строке " + i +  " указан неверный ключ музыкальной группы.");
                }

                Long id = Long.parseLong(record.get(1));
                if (new IdValidator().validate(id)) {
                    if (idList.contains(id)) {
                        throw new IdExistsException("Музыкальная группа с id " + id + " уже существует.");
                    }
                    idList.add(id);
                }
                else {
                    throw new WrongValueException("В строке " + i +  " указан неверный ключ музыкальной группы.");
                }

                String name = record.get(2);
                if (!new NameValidator().validate(name)) {
                    throw new WrongValueException("В строке " + i +  " указано неверное название музыкальной группы.");
                }

                Integer coordX = Integer.parseInt(record.get(3));
                if (!new CoordXValidator().validate(coordX)) {
                    throw new WrongValueException("В строке " + i +  " указана неверная координата x музыкальной группы.");
                }

                long coordY = Long.parseLong(record.get(4));
                if (!new CoordYValidator().validate(coordY)) {
                    throw new WrongValueException("В строке " + i +  " указана неверная координата y музыкальной группы.");
                }

                Coordinates coordinates = new Coordinates(coordX, coordY);

                Date creationDate = new Date(Long.parseLong(record.get(5)));
                if (!new CreationDateValidator().validate(creationDate)) {
                    throw new WrongValueException("В строке " + i +  " указана неверная дата создания элемента класса MusicBand.");
                }

                Integer numberOfParticipants = Integer.parseInt(record.get(6));
                if (!new NumberOfParticipantsValidator().validate(numberOfParticipants)) {
                    throw new WrongValueException("В строке " + i +  " указано неверное количество участников музыкальной группы.");
                }

                MusicGenre genre;
                try {
                    if (record.get(7) == null) {
                        genre = null;
                    }
                    else{
                        genre = MusicGenre.valueOf(record.get(7));
                    }
                } catch (IllegalArgumentException e) {
                    throw new WrongValueException("В строке " + i +  " указан неверный жанр музыкальной группы.");
                }

                Album bestAlbum;
                if (record.get(8) == null) {
                    bestAlbum = null;
                }
                else {
                    String albumName = record.get(8);
                    if (!new AlbumNameValidator().validate(albumName)) {
                        throw new WrongValueException("В строке " + i +  " указано неверное название музыкального альбома.");
                    }
                    Double albumSales = Double.valueOf(record.get(9));
                    if (!new AlbumSalesValidator().validate(albumSales)) {
                        throw new WrongValueException("В строке " + i +  " указано неверное число продаж музыкального альбома.");
                    }
                    bestAlbum = new Album(albumName, albumSales);
                }
                MusicBand musicBand = new MusicBand(id, name, coordinates, creationDate, numberOfParticipants, genre, bestAlbum);
                collection.put(key, musicBand);
            } catch (Exception e) {
                System.out.println("Ошибка при обработке строки: " + e.getMessage());
                System.out.println("Строка с ошибкой пропущена.");
            }
        }
        return collection;
    }

    /**
     * Переводит коллекцию объектов класса MusicBand в набор строк для файла формата csv.
     * @param collection коллекция объектов класса MusicBand
     * @return строки файла в формате csv
     */
    public static List<String> parseCollectionToCSV(HashMap<Integer, MusicBand> collection) {
        List<String> lines = new ArrayList<>();
        String[] headers = {"key", "id", "name", "coordinateX", "coordinateY", "creationDate",
                            "numberOfParticipants", "genre", "albumName", "albumSales"};

        try (StringWriter writer = new StringWriter();
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers))) {

            for (Map.Entry<Integer, MusicBand> entry : collection.entrySet()) {
                csvPrinter.printRecord(
                        entry.getKey(),
                        entry.getValue().getId(),
                        entry.getValue().getName(),
                        entry.getValue().getCoordinates().getX(),
                        entry.getValue().getCoordinates().getY(),
                        entry.getValue().getCreationDate().getTime(),
                        entry.getValue().getNumberOfParticipants(),
                        (entry.getValue().getGenre() != null) ? entry.getValue().getGenre().toString() : "",
                        (entry.getValue().getBestAlbum() != null) ? entry.getValue().getBestAlbum().getName() : "",
                        (entry.getValue().getBestAlbum() != null) ? String.valueOf(entry.getValue().getBestAlbum().getSales()) : ""
                );
            }

            csvPrinter.flush();
            String csvContent = writer.toString();

            String[] csvLines = csvContent.split("\\r?\\n");
            for (String line : csvLines) {
                lines.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }
}
