package filemanager.json;

import dataclasses.MusicBand;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Reads a JSON file and deserializes its content into a Hashtable of MusicBand objects keyed by Integer.
 * Implements the Singleton pattern to ensure a single instance handles file reading operations.
 */
public class JsonReader {

    private static JsonReader instance;
    /**
     * The name of the JSON file to read from.
     */
    private final String fileName;

    /**
     * Private constructor that sets the file name.
     *
     * @param filename the name of the JSON file
     */
    private JsonReader(String filename) {
        this.fileName = filename;
    }

    /**
     * Returns the singleton instance of JsonReader.
     *
     * @return the singleton instance
     * @throws IllegalStateException if the instance has not been initialized
     */
    public static JsonReader getInstance() {
        if (instance == null) {
            throw  new IllegalStateException("JsonReader has not been initialized");
        }
        return instance;
    }

    /**
     * Initializes the singleton instance with the specified file name.
     * Must be called once before using the instance.
     *
     * @param fileName the name of the JSON file
     * @throws IllegalStateException if the instance has already been initialized
     */
    public static void initialize(String fileName) {
        if (instance == null) {
            instance = new JsonReader(fileName);
        }
        else {
            throw new IllegalStateException("JsonReader has already been initialized");
        }
    }

    /**
     * Reads the JSON file and deserializes its contents into a Hashtable of MusicBand objects.
     * If the file does not exist or is empty, returns an empty Hashtable.
     *
     * @return a Hashtable containing the deserialized MusicBand objects (keyed by Integer)
     * @throws RuntimeException if an I/O error occurs during reading
     */
    public Hashtable<Integer, MusicBand> readFile() {

        // Если файл не существует или пустой, возвращаем пустую таблицу
        if (!new File(fileName).exists() || (new File(fileName).length() == 0)) {
            return new Hashtable<>();
        }

        try (FileInputStream file = new FileInputStream(fileName)) {

            Hashtable<Integer, MusicBand> map = JsonDataMapper.getInstance().getMapper().readValue(file, new TypeReference<Hashtable<Integer, MusicBand>>() {
            });

            return map;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}