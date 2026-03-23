package filemanager.json;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Provides functionality to clean a JSON file by truncating its content.
 * Implements the Singleton pattern to ensure a single instance manages the cleaning operation.
 */
public class JsonCleaner {

    /**
     * The name of the file to be cleaned.
     */
    private final String fileName;
    private static JsonCleaner instance;

    /**
     * Private constructor that sets the file name.
     *
     * @param fileName the name of the file to clean
     */
    private JsonCleaner(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns the singleton instance of JsonCleaner.
     *
     * @return the singleton instance
     * @throws IllegalStateException if the instance has not been initialized
     */
    public static JsonCleaner getInstance() {
        if (instance == null) {
            throw new IllegalStateException("JsonCleaner has not been initialized");
        }
        return instance;

    }

    /**
     * Initializes the singleton instance with the specified file name.
     * Must be called once before using the instance.
     *
     * @param fileName the name of the file to clean
     * @throws IllegalStateException if the instance has already been initialized
     */
    public static void initialize(String fileName) {
        if (instance == null) {
            instance = new JsonCleaner(fileName);
        }
        else throw new IllegalStateException("JsonCleaner has been already initialized");

    }

    /**
     * Attempts to clean the file by opening it in append mode and writing nothing.
     * Note: Opening with `true` (append mode) does not truncate the file; the actual behavior
     * may not result in cleaning as expected. This method currently does not write any data.
     *
     * @throws RuntimeException if an I/O error occurs
     */
    public void cleanFile() {
        try (FileOutputStream file = new FileOutputStream(fileName, true)) {
            //Ничего не вызываем. Перезаписываем файл в нулевой
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}