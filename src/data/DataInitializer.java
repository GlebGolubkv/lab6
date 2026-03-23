package data;

import filemanager.commandsparser.CommandsReader;
import filemanager.json.*;
import termenalmanager.BandsFileReader;
import termenalmanager.BandsInputTerminal;

/**
 * Centralizes the initialization of all application components.
 * Implements the Singleton pattern to ensure a single initialization point.
 * This class is responsible for setting up various managers and readers required
 * for the application's operation, including JSON processing, command handling,
 * terminal input, and the main collection manager.
 */
public class DataInitializer {

    /**
     * The singleton instance of DataInitializer.
     */
    public static DataInitializer instance;

    /**
     * Private constructor that performs the actual initialization of all components.
     * It calls the initialize methods of various managers and readers, passing
     * the provided file name where necessary.
     *
     * @param fileName the name of the file used for initializing components like
     *                 JsonWriter, JsonReader, JsonDateReader, JsonCleaner, and ClassesManager
     */
    private DataInitializer(String fileName) {

        JsonDataMapper.initialize();
        DataCommands.initialize();
        BandsInputTerminal.initialize();
        BandsFileReader.initialize();
        CommandsReader.initialize();
        JsonWriter.initialize(fileName);
        JsonReader.initialize(fileName);
        JsonParser.initialize();
        JsonDateReader.initialize(fileName);
        JsonCleaner.initialize(fileName);
        ClassesManager.initialize();

    }

    /**
     * Returns the singleton instance of DataInitializer.
     *
     * @return the singleton instance
     * @throws RuntimeException if the instance has not been initialized yet
     */
    public static DataInitializer getInstance() {
        if (instance == null) {
            throw new RuntimeException("data has not been initialized");
        }
        return instance;
    }

    /**
     * Initializes the singleton instance with the specified file name.
     * This method must be called once before using the instance.
     *
     * @param fileName the name of the file used for initialization
     * @throws RuntimeException if the instance has not been initialized (due to incorrect error message logic)
     */
    public static void initialize(String fileName) {
        if (instance == null) {
            instance = new DataInitializer(fileName);
        } else {
            throw new RuntimeException("data has not been initialized");
        }
    }
}