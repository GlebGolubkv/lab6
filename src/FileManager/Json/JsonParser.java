package FileManager.Json;

import DataClasses.MusicBand;

import java.util.Hashtable;

/**
 * Класс, через который выполняется вся работа с файлом
 * <p>
 *     Этот класс объединяет в себе методы JsonReader, JsonWriter, JsonDateReader, JsonCleaner для удобства обращения к
 *     их функциям
 * </p>
 */
public class JsonParser {

    String fileName;

    public JsonParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Метод, позволяющий записать один объект MusicBand в класс, при этом сохранив все ранее записанное в файе
     * @param key
     * @param musicBand
     */

    public void WriteOneClassToFile(int key, MusicBand musicBand) {
        new JsonWriter(fileName, key, musicBand);

    }

    /**
     * Метод, позволяющий записать в файл весь список данных. При этом все находящееся в файле будет перезаписано.
     * @param Map
     */
    public void WriteLibraryToFile(Hashtable<Integer, MusicBand> Map) {
        new JsonWriter(fileName, Map);
    }

    /**
     * Метод считывает все содержимое файла
     * @return Hashtable, на основе которого производится вся последующая работа
     */

    public Hashtable<Integer, MusicBand> ReadAllClassesAtFile() {
        return new JsonReader(fileName).readFile();
    }

    /**
     * Метод отчищает файл
     */
    public void CleanFile() {
        new JsonCleaner(fileName).cleanFile();

    }

    /**
     * Метод, который считывает дату создания файла
     * @return дата создания файла
     */
    public String getCreationTimeOfFile() {
        return new JsonDateReader(fileName).getCreationTime();
    }



}
