package filemanager.json;

import termenalmanager.Colors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Provides functionality to retrieve the creation time of a JSON file.
 * Implements the Singleton pattern to ensure a single instance manages the file date reading.
 */
public class JsonDateReader {

    private static JsonDateReader instance;
    /**
     * The name of the file whose creation time will be read.
     */
    private final String fileName;

    /**
     * Private constructor that sets the file name.
     *
     * @param fileName the name of the file
     */
    private JsonDateReader(String fileName) {
        this.fileName = fileName;

    }

    /**
     * Returns the singleton instance of JsonDateReader.
     *
     * @return the singleton instance
     * @throws IllegalStateException if the instance has not been initialized
     */
    public static JsonDateReader getInstance() {
        if (instance == null) {
            throw new IllegalStateException("JsonDateReader has not been initialized");
        }
        return instance;
    }

    /**
     * Initializes the singleton instance with the specified file name.
     * Must be called once before using the instance.
     *
     * @param fileName the name of the file
     * @throws IllegalStateException if the instance has already been initialized
     */
    public static void initialize(String fileName) {
        if (instance == null) {
            instance = new JsonDateReader(fileName);
        }
        else throw new IllegalStateException("JsonDateReader has been initialized");
    }

    /**
     * Retrieves the creation time of the file and returns it as a formatted string with color.
     *
     * @return a colored string containing the creation date, or an error message if unsupported
     * @throws RuntimeException if an I/O error occurs
     */
    public String getCreationTime() {
        Path path = Paths.get(fileName);

        try {
            FileTime creationTime = (FileTime) Files.getAttribute(path, "creationTime");
            ZonedDateTime created = ZonedDateTime.ofInstant(
                    creationTime.toInstant(), ZoneId.systemDefault());
            return (Colors.GREEN +  "Creation date: " + Colors.RESET + created);
        } catch (UnsupportedOperationException e) {
            return ("it is impossible to make an operation");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}