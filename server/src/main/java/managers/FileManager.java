package main.java.managers;

import entity.MusicBand;
import utility.ParserCSV;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Класс, отвечающий за взаимодействие с файлами.
 * @author Alina
 */
public class FileManager {
    private Path collectionFilePath;

    /**
     * Конструктор
     * @param collectionFilePath имя файла, из которого читается и в который сохраняется коллекция
     */
    public FileManager(Path collectionFilePath) {
        this.collectionFilePath = collectionFilePath;
    }

    /**
     * Читает коллекцию объектов класса MusicBand из файла.
     * @return коллекция объектов
     * @throws IOException исключение, возникающее, если невозможно прочитать файл
     */
    public HashMap<Integer, MusicBand> readCollection() throws IOException {
        HashMap<Integer, MusicBand> collection;
        try (InputStreamReader collectionInputStreamReader = 
            new InputStreamReader(new FileInputStream(this.collectionFilePath.toAbsolutePath().toString()))) {
            ArrayList<String> fileLines = readAllLines(collectionInputStreamReader);
            collection = ParserCSV.parseFromCSV(fileLines);
        }
        return collection;
    }

    /**
     * Читает все строки из потока.
     * @param inputStreamReader поток, из которого читаются строки
     * @return список строк
     * @throws IOException исключение, возникающее, если невозможно прочитать строку
     */
    public ArrayList<String> readAllLines(InputStreamReader inputStreamReader) throws IOException {
        ArrayList<String> lines;
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            lines = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        }
        return lines;
    }

    /**
     * Записывает в файл коллекцию объектов класса MusicBand.
     * @param collection коллекция объектов класса MusicBand
     * @throws IOException исключение, возникающее, если невозможно записать строку
     */
    public void writeCollection(HashMap<Integer, MusicBand> collection) throws IOException {
        List<String> fileLines = ParserCSV.parseToCSV(collection);
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(this.collectionFilePath.toAbsolutePath().toString()))) {
            writeAllLines(writer, fileLines);
            writer.flush();
        }
    }

    /**
     * Пишет в поток все переданные строки.
     * @param writer поток, в который пишутся строки
     * @param lines строки, которые нужно запиасать
     * @throws IOException исключение, возникающее, если невозможно записать строку
     */
    public void writeAllLines(OutputStreamWriter writer, List<String> lines) throws IOException {
        for (String line : lines) {
            writer.write(line + "\n");
        }
        writer.flush();
    }

    /**
     * Проверяет, существует ли файл с данным именем.
     * @param filePath имя файла
     * @return true - если файл существует, иначе false
     */
    public boolean isFileExist(Path filePath) {
        File file = new File(filePath.toString());
        return file.exists();
    }

    /**
     * Устанавливает новое имя файла коллекции
     * @param collectionFilePath новое имя файла коллекции
     */
    public void setCollectionFileName(Path collectionFilePath) {
        this.collectionFilePath = collectionFilePath;
    }

    /**
     * Возвращает текущее имя файла коллекции
     * @return текущее имя файла коллекции
     */
    public Path getCollectionFilePath () {
        return collectionFilePath;
    }
}
