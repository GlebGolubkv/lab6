package server.data;


import common.dataclasses.MusicBand;

import common.dataclasses.Colors;
import server.postgres.CommandsDAO;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Manages the collection of MusicBand objects, providing access and modification methods.
 * Implements the Singleton pattern to ensure a single instance of the collection manager.
 * The collection is stored in a Hashtable with Integer keys and MusicBand values.
 */
public class ClassesManager {
    private static ClassesManager instance;

    private final Map<Integer, MusicBand> collection = Collections.synchronizedMap(new HashMap<Integer, MusicBand>());


    /**
     * Private constructor that initializes the collection by reading data from a file.
     */
    private ClassesManager() {
        collection.putAll(CommandsDAO.readFromPostgres());
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
    public Map<Integer, MusicBand> getCollection() {
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
        synchronized (collection) {
            StringBuilder s = new StringBuilder();
            for (Integer key : getActiveMap().keySet()) {
                s.append(Colors.GREEN)
                        .append("Key: ").append(Colors.RESET).append(key).append("\n")
                        .append(Colors.GREEN).append(" Value: ").append(Colors.RESET)
                        .append(getActiveMap().get(key).toString()).append("\n");

            }
            return s.toString();
        }
    }

    public String showCollection() {
        synchronized (collection) {
            return getActiveMap().keySet()
                    .stream()
                    .sorted((a, b) -> (getActiveMap().get(b).getName().compareTo(getActiveMap().get(a).getName())))
                    .map(key -> Colors.GREEN + "Key: " + Colors.RESET + key + Colors.GREEN +
                            "\nValue: " + Colors.RESET + getActiveMap().get(key).toString() + Colors.RESET + "\n")
                    .collect(Collectors.joining());
        }

    }


    private Map<Integer, MusicBand> getActiveMap() {
        return collection;
    }

}