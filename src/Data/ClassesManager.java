package Data;

import DataClasses.MusicBand;
import FileManager.Json.JsonParser;
import TernemalManager.Colors;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Класс, который реализует всю работу с данными
 * <p>
 *     Один из основных классов программы, который при первой инициализации считывает данные из файла,
 *     а далее работает с ними.
 * </p>
 */
public class ClassesManager {

    public static Hashtable<Integer, MusicBand> Map = new Hashtable<>();
    public static String fileName;
    public HashMap<Integer, MusicBand> getMap;

    /**
     * Пустой конструктор, нужный для обычной инициализации
     */
    public ClassesManager() {
    }

    /**
     * Конструктор, необходимый для первоначальной инициализации в начале работы программы. Считывает данные из файла
     * @param fileName
     */
    public ClassesManager(String fileName) {

        ClassesManager.fileName = fileName;
        Map = new JsonParser(fileName).ReadAllClassesAtFile();
    }

    /**
     * Метод получаения размера коллекции
     * @return размер коллекции
     */
    public int MapSize() {
        return Map.size();
    }

    /**
     * Метод получения даты создания файла
     * @return дата создания файла
     */
    public String GetCreationDate() {
        return new JsonParser(fileName).getCreationTimeOfFile();
    }



    public String GetCollectionType() {
        return Colors.GREEN + "Collection Type: " + Colors.RESET + Map.getClass().getName();
    }

    /**
     * Метод, позволяющий получить копию коллекции
     * @return копия коллекции
     */
    public static Hashtable<Integer, MusicBand> getCopyOfMap() {
        return new Hashtable<>(Map); // копия
    }

    /**
     * Метод, позволяющий получить коллекцию с последующей возможностью ее изменения
     * @return коллекция
     */
    public Hashtable<Integer, MusicBand> GetMap() {
        return Map;
    }

    public void addMusicBandToCollection(int key, MusicBand mb) {
        Map.put(key, mb);
    }

    public void removeMusicBandFromCollection(int key) {
        Map.remove(key);
    }

    public boolean keyInMap(int key) {
        return Map.containsKey(key);
    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Integer key : Map.keySet()) {
            s.append(Colors.GREEN)
                    .append("Key: ").append(Colors.RESET).append(key).append("\n")
                    .append(Colors.GREEN).append(" Value: ").append(Colors.RESET)
                    .append(Map.get(key).toString()).append("\n");

        }
        return s.toString();
    }

    /**
     * Метод, который сохраняет коллекцию в файл
     */
    public void SaveCollectionToFile() {
        JsonParser jsonParser = new JsonParser(fileName);
        jsonParser.WriteLibraryToFile(Map);
    }

    /**
     *  Метод, который очищает коллекцию
     */
    public void ClearCollection() {
        Map.clear();
    }

}
