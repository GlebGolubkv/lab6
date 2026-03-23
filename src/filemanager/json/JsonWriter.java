package filemanager.json;

import dataclasses.MusicBand;

import java.io.*;
import java.util.Hashtable;

/**
 * Provides functionality to write JSON data to a file.
 * Handles writing single MusicBand entries or entire collections (Hashtables) to the specified file.
 * Implements the Singleton pattern to ensure a single instance manages write operations.
 */
public class JsonWriter {

    private static JsonWriter instance;
    /**
     * The name of the JSON file to write to.
     */
    private final String fileName;

    /**
     * Private constructor that sets the file name.
     *
     * @param fileName the name of the JSON file
     */
    private JsonWriter(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns the singleton instance of JsonWriter.
     *
     * @return the singleton instance
     * @throws IllegalStateException if the instance has not been initialized
     */
    public static JsonWriter getInstance() {
        if (instance == null) {
            throw new IllegalStateException("JsonWriter has not been initialized");
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
            instance = new JsonWriter(fileName);
        } else {
            throw new IllegalStateException("JsonWriter has already been initialized");
        }
    }

    /**
     * Writes a single MusicBand object associated with a key to the file.
     * Reads the existing content, updates it with the new key-band pair, and writes the entire map back.
     *
     * @param key       the key associated with the MusicBand
     * @param musicBand the MusicBand object to write
     */
    public void writeValue(int key, MusicBand musicBand) {

        //в oldMap записываем копию текущего файла
        Hashtable<Integer, MusicBand> oldMap = JsonReader.getInstance().readFile();
        oldMap.put(key, musicBand);

        // создает поток записи в файл. Перезаписывает с нуля
        try (FileOutputStream file = new FileOutputStream(fileName)) {

            //записываем в файл
            JsonDataMapper.getInstance().getMapper().writeValue(file, oldMap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the entire collection (Hashtable) of MusicBand objects to the file,
     * overwriting any existing content.
     *
     * @param newMap the Hashtable containing the collection to write (keyed by Integer)
     */
    public void writeMap(Hashtable<Integer, MusicBand> newMap) {

        // создает поток записи в файл. Перезаписывает с нуля
        try (FileOutputStream file = new FileOutputStream(fileName)) {

            //записываем в файл
            JsonDataMapper.getInstance().getMapper().writeValue(file, newMap);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}