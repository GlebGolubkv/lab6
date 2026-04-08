package server.data;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.dataclasses.MusicBand;
import common.JsonDataMapper;
import server.filemanager.JsonParser;
import common.dataclasses.Colors;


import java.util.Hashtable;
import java.util.stream.Collectors;


/**
 * Manages the collection of MusicBand objects, providing access and modification methods.
 * Implements the Singleton pattern to ensure a single instance of the collection manager.
 * The collection is stored in a Hashtable with Integer keys and MusicBand values.
 */
public class ClassesManager {                           // переписать метод tostring в отдельный метод
    private static ClassesManager instance;
    private final Hashtable<Integer, MusicBand> Map;
    private boolean inTransaction = false;
    private Hashtable<Integer, MusicBand> tempMap;


    /**
     * Private constructor that initializes the collection by reading data from a file.
     */
    private ClassesManager() {
        Map = JsonParser.getInstance().readAllClassesAtFile();
    }

    /**
     * Returns the singleton instance of ClassesManager.
     *
     * @return the singleton instance
     * @throws RuntimeException if the manager has not been initialized yet
     */
    public static ClassesManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("ClassesManager not initialized");
        }
        return instance;
    }

    /**
     * Initializes the singleton instance with the specified file name.
     *
     * @throws RuntimeException if the manager is already initialized
     */
    public static synchronized void initialize() {


        if (instance == null) {
            instance = new ClassesManager();
        } else {
            throw new RuntimeException("ClassesManager already initialized");
        }


    }


    /**
     * Returns the current size of the collection.
     *
     * @return the number of elements in the collection
     */
    public int mapSize() {
        return getActiveMap().size();
    }


    /**
     * Returns the creation time of the underlying data file.
     *
     * @return a string representing the file's creation time
     */
    public String getCreationDate() {
        return JsonParser.getInstance().getCreationTimeOfFile();
    }


    /**
     * Returns a formatted string describing the type of the collection.
     *
     * @return a colored string with the collection type information
     */
    public String getCollectionType() {
        return Colors.GREEN + "Collection Type: " + Colors.RESET + getActiveMap().getClass().getName();
    }


    /**
     * Returns the underlying Hashtable that stores the collection.
     *
     * @return the Hashtable containing MusicBand objects keyed by Integer
     */
    public Hashtable<Integer, MusicBand> getMap() {
        return getActiveMap();
    }

    /**
     * Adds a MusicBand object to the collection with the specified key.
     *
     * @param key the key associated with the MusicBand
     * @param mb  the MusicBand object to add
     */
    public void addMusicBandToCollection(int key, MusicBand mb) {
        getActiveMap().put(key, mb);
    }

    /**
     * Removes the MusicBand object associated with the specified key from the collection.
     *
     * @param key the key of the MusicBand to remove
     */
    public void removeMusicBandFromCollection(int key) {
        getActiveMap().remove(key);
    }

    /**
     * Checks whether the specified key exists in the collection.
     *
     * @param key the key to check
     * @return true if the key is present, false otherwise
     */
    public boolean keyInMap(int key) {
        return getActiveMap().containsKey(key);
    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for (Integer key : getActiveMap().keySet()) {
            s.append(Colors.GREEN)
                    .append("Key: ").append(Colors.RESET).append(key).append("\n")
                    .append(Colors.GREEN).append(" Value: ").append(Colors.RESET)
                    .append(getActiveMap().get(key).toString()).append("\n");

        }
        return s.toString();
    }

    public String showCollection() {
        return getActiveMap().keySet()
                .stream()
                .sorted((a, b) -> (getActiveMap().get(b).getName().compareTo(getActiveMap().get(a).getName())))
                .map(key -> Colors.GREEN + "Key: " + Colors.RESET + key + Colors.GREEN +
                        "\nValue: " + Colors.RESET + getActiveMap().get(key).toString() + Colors.RESET + "\n")
                .collect(Collectors.joining());

    }


    /**
     * Saves the current collection to the file using JsonParser.
     */
    public void saveCollectionToFile() {
        if (inTransaction) {
            commitTransaction();
        }
        JsonParser jsonParser = JsonParser.getInstance();
        jsonParser.writeLibraryToFile(Map);
    }


    /**
     * Removes all elements from the collection.
     */
    public void clearCollection() {
        getActiveMap().clear();
    }


    private Hashtable<Integer, MusicBand> deepCopy(Hashtable<Integer, MusicBand> original) {
        try {
            ObjectMapper mapper = JsonDataMapper.getInstance().getMapper();
            String json = mapper.writeValueAsString(original);
            return mapper.readValue(json, new TypeReference<Hashtable<Integer, MusicBand>>() {
            });


        } catch (Exception e) {
            throw new RuntimeException("Error: error creating a deep copy", e);
        }
    }


    public boolean isInTransaction() {
        return inTransaction;
    }


    private Hashtable<Integer, MusicBand> getActiveMap() {
        if (isInTransaction()) {
            return tempMap;
        } else {
            return Map;
        }
    }

    public void beginTransaction() {
        if (isInTransaction()) {
            throw new RuntimeException("Transaction is already in progress");
        }
        tempMap = deepCopy(Map);
        inTransaction = true;

    }


    public void commitTransaction() {
        if (!isInTransaction()) {
            throw new RuntimeException("Transaction is not in progress");
        }
        Map.clear();
        Map.putAll(tempMap);
        tempMap = null;
        inTransaction = false;


    }

    public void rollbackTransaction() {
        if (!isInTransaction()) {
            throw new RuntimeException("Transaction is not in progress");
        }
        tempMap = null;
        inTransaction = false;

    }
}