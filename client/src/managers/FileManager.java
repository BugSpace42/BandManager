package managers;

import entity.MusicBand;
import utility.ParserCSV;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// написать методы openFile, closeFile, readCollection, writeCollection

/**
 * Класс, отвечающий за взаимодействие с файлами.
 * @author Alina
 */
public class FileManager {
    private Path collectionFilePath;

    /**
     * Конструктор
     * @param collectionFileName имя файла, из которого читается и в который сохраняется коллекция
     */
    public FileManager(Path collectionFilePath) {
        this.collectionFilePath = collectionFilePath;
    }

    /**
     * Читает коллекцию объектов класса MusicBand из файла.
     * @return коллекция объектов
     * @throws IOException исключение, возникающее, если невозможно прочитать файл
     */
    /*
     * Объясню, почему я пробрасываю исключение, а не обрабатываю его здесь.
     * Этот метод отвечает лишь за то, чтобы читать из коллекции. Он не знает, что делать, если не получилось.
     * Взаимодействием с пользователем занимаются другие классы, в них и будет описана логика этих действий.
     * Чтение коллекции может быть реализовано в разных частях программы, и исключения при чтении файла там могут быть
     * реализованы по-разному. 
     * Пока что мне кажется, что будет правильно предупредить пользователя о том, что прочитать файл не удалось, 
     * и создать новую коллекцию. Однако можно и сказать пользователю, что мы не можем прочитать файл,
     * и попросить ввести другое имя файла. 
     * Аналогичная ситуация и с методом записи коллекции в файл.
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
    /*
     * Причины, по которым я пробрасываю исключение, описаны в комментарии к readCollection().
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
     * @param outputStreamWriter поток, в который пишутся строки
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
     * @param fileName имя файла
     * @return true - если файл существует, иначе false
     */
    public boolean isFileExist(Path filePath) {
        File file = new File(filePath.toString());
        return file.exists();
    }

    /**
     * Устанавливает новое имя файла коллекции
     * @param newCollectionFileName новое имя файла коллекции
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
