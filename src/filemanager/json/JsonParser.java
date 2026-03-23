package filemanager.json;

import dataclasses.MusicBand;

import java.util.Hashtable;

/**
 * Provides high-level JSON operations for reading and writing MusicBand collections.
 * Acts as a facade, delegating calls to specific JSON handlers (JsonWriter, JsonReader, JsonCleaner, JsonDateReader).
 * Implements the Singleton pattern to ensure a single point of access.
 */
public class JsonParser {

    private static JsonParser instance;

    /**
     * Initializes the singleton instance of JsonParser.
     * Must be called once before using the instance.
     *
     * @throws IllegalStateException if the instance has already been initialized
     */
    public static void initialize()
    {
        if (instance == null) {
            instance = new JsonParser();
        }
        else throw new IllegalStateException("JsonParser has been already initialized");
    }

    /**
     * Returns the singleton instance of JsonParser.
     *
     * @return the singleton instance
     * @throws IllegalStateException if the instance has not been initialized
     */
    public static JsonParser getInstance(){
        if (instance == null) {
            throw new IllegalStateException("JsonParser has not been initialized");
        }
        return instance;
    }

    /**
     * Private constructor for singleton pattern.
     */
    private JsonParser() {

    }

    /**
     * Writes a single MusicBand object associated with a specific key to the JSON file.
     * Delegates to {@link JsonWriter#writeValue(int, MusicBand)}.
     *
     * @param key        the key associated with the MusicBand
     * @param musicBand  the MusicBand object to write
     */
    public void writeOneClassToFile(int key, MusicBand musicBand) {
        JsonWriter.getInstance().writeValue( key, musicBand);

    }

    /**
     * Writes the entire collection (Hashtable) of MusicBand objects to the JSON file.
     * Delegates to {@link JsonWriter#writeMap(Hashtable)}.
     *
     * @param Map the Hashtable containing the collection (keyed by Integer)
     */
    public void writeLibraryToFile(Hashtable<Integer, MusicBand> Map) {
        JsonWriter.getInstance().writeMap(Map);
    }

    /**
     * Reads all MusicBand objects from the JSON file and returns them as a Hashtable.
     * Delegates to {@link JsonReader#readFile()}.
     *
     * @return a Hashtable containing the deserialized MusicBand objects
     */
    public Hashtable<Integer, MusicBand> readAllClassesAtFile() {
        return JsonReader.getInstance().readFile();
    }

    /**
     * Cleans (truncates) the JSON file by delegating to {@link JsonCleaner#cleanFile()}.
     */
    public void cleanFile() {
        JsonCleaner.getInstance().cleanFile();
    }

    /**
     * Retrieves the creation time of the JSON file.
     * Delegates to {@link JsonDateReader#getCreationTime()}.
     *
     * @return a string containing the creation time (possibly colored)
     */
    public String getCreationTimeOfFile() {
        return JsonDateReader.getInstance().getCreationTime();
    }

}