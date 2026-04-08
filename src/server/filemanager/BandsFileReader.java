package server.filemanager;

import server.data.generators.IDGenerator;
import common.dataclasses.Coordinates;
import common.dataclasses.Label;
import common.dataclasses.MusicBand;
import common.dataclasses.MusicGenre;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * Provides functionality to read MusicBand objects from a file using a BufferedReader.
 * Handles reading of all MusicBand fields line by line and constructs the corresponding object.
 * Implements the Singleton pattern to ensure a single instance manages file-based band input.
 */
public class BandsFileReader {

    private static BandsFileReader instance;

    /**
     * Private constructor for singleton pattern.
     */
    private BandsFileReader() {
    }

    /**
     * Returns the singleton instance of BandsFileReader.
     *
     * @return the singleton instance
     * @throws IllegalStateException if the instance has not been initialized
     */
    public static BandsFileReader getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BandsInputFromFile has not been initialized");
        }
        return instance;
    }

    /**
     * Initializes the singleton instance of BandsFileReader.
     * Must be called once before using the instance.
     *
     * @throws IllegalStateException if the instance has already been initialized
     */
    public static void initialize() {
        if (instance == null) {
            instance = new BandsFileReader();
        } else {
            throw new IllegalStateException("BandsInputFromFile has already been initialized");
        }
    }

    /**
     * Reads a MusicBand object from the provided BufferedReader, generating a new ID automatically.
     * Reads all required fields in a predefined order: name, x-coordinate, y-coordinate,
     * number of participants, albums count, music genre, and label bands count.
     *
     * @param bufferedReader the BufferedReader to read data from
     * @return a fully constructed MusicBand object
     * @throws IllegalArgumentException if any error occurs during reading or validation
     */
    public MusicBand inputBand(BufferedReader bufferedReader) {

        try {

            Integer Id = new IDGenerator().generateNewID(); // ID

            String Name = readName(bufferedReader);  //NAME

            Coordinates coordinates = new Coordinates(readXCoordinates(bufferedReader), readYCoordinates(bufferedReader)); // COORDINATES

            ZonedDateTime time = ZonedDateTime.now(); // TIME

            long numberOfParticipants = readNumberOfParticipants(bufferedReader); // participants

            long albumsCount = readAlbumsCount(bufferedReader); // albums

            MusicGenre musicGenre = readMusicGenre(bufferedReader); // genre

            Label label = readLabel(bufferedReader); // label

            MusicBand musicBand = new MusicBand(Id, Name, coordinates, time, numberOfParticipants, albumsCount, musicGenre, label);

            return musicBand;
        } catch (Exception e) {

            throw new IllegalArgumentException("Error reading from file.");

        }
    }

    /**
     * Reads a MusicBand object from the provided BufferedReader, using the specified ID.
     * Reads all required fields in a predefined order (same as {@link #inputBand(BufferedReader)}).
     *
     * @param Id              the ID to assign to the MusicBand
     * @param bufferedReader the BufferedReader to read data from
     * @return a fully constructed MusicBand object with the given ID
     * @throws IllegalArgumentException if any error occurs during reading or validation
     */
    public MusicBand inputBand(Integer Id, BufferedReader bufferedReader) {

        try {

            String Name = readName(bufferedReader);  //NAME

            Coordinates coordinates = new Coordinates(readXCoordinates(bufferedReader), readYCoordinates(bufferedReader)); // COORDINATES

            ZonedDateTime time = ZonedDateTime.now(); // TIME

            long numberOfParticipants = readNumberOfParticipants(bufferedReader); // participants

            long albumsCount = readAlbumsCount(bufferedReader); // albums

            MusicGenre musicGenre = readMusicGenre(bufferedReader); // genre

            Label label = readLabel(bufferedReader); // label

            MusicBand musicBand = new MusicBand(Id, Name, coordinates, time, numberOfParticipants, albumsCount, musicGenre, label);


            return musicBand;
        } catch (Exception e) {

            throw new IllegalArgumentException("Error reading from file.");

        }
    }

    /**
     * Reads and validates the band name from the BufferedReader.
     *
     * @param bufferedReader the BufferedReader to read from
     * @return the trimmed non-empty name
     * @throws IOException           if an I/O error occurs
     * @throws IllegalStateException if the name is empty
     */
    private String readName(BufferedReader bufferedReader) throws IOException {
        String name = bufferedReader.readLine().trim();
        if (name.isEmpty()) {
            throw new IllegalStateException("Error: name cannot be empty.");
        } else return name;
    }

    /**
     * Reads and validates the x-coordinate from the BufferedReader.
     *
     * @param bufferedReader the BufferedReader to read from
     * @return the x-coordinate as an integer (must be ≤ 254)
     * @throws IOException           if an I/O error occurs
     * @throws IllegalStateException if the input is empty, not an integer, or exceeds 254
     */
    private Integer readXCoordinates(BufferedReader bufferedReader) throws IOException {

        String x = bufferedReader.readLine().trim();

        if (x.isEmpty()) {

            throw new IllegalStateException("Error: coordinate cannot be empty.");
        } else try {
            int x1 = Integer.parseInt(x);
            if (x1 <= 254) {
                return x1;
            } else {

                throw new IllegalStateException("Error: coordinate must be less then 254.");
            }
        } catch (NumberFormatException e) {

            throw new IllegalStateException("Error: coordinate must be an integer.");
        }

    }

    /**
     * Reads and validates the y-coordinate from the BufferedReader.
     *
     * @param bufferedReader the BufferedReader to read from
     * @return the y-coordinate as a double (must be ≤ 93)
     * @throws IOException                if an I/O error occurs
     * @throws IllegalArgumentException if the input is empty, not a number, or exceeds 93
     */
    private Double readYCoordinates(BufferedReader bufferedReader) throws IOException {

        String y = bufferedReader.readLine().trim();

        if (y.isEmpty()) {

            throw new IllegalStateException("Error: coordinate cannot be empty.");
        } else {
            try {
                Double y1 = Double.parseDouble(y);
                if (y1 <= 93) {
                    return y1;
                } else {

                    throw new IllegalArgumentException("Error: coordinate must be less then 93.");
                }
            } catch (NumberFormatException e) {

                throw new IllegalArgumentException("Error: coordinate must be an integer.");
            }
        }

    }

    /**
     * Reads and validates the number of participants from the BufferedReader.
     *
     * @param bufferedReader the BufferedReader to read from
     * @return the number of participants (must be > 0)
     * @throws IOException                if an I/O error occurs
     * @throws IllegalArgumentException if the input is empty, not a valid long, or ≤ 0
     */
    private long readNumberOfParticipants(BufferedReader bufferedReader) throws IOException {

        String n = bufferedReader.readLine().trim();

        if (n.isEmpty()) {

            throw new IllegalArgumentException("Error: Number cannot be empty.");
        } else try {
            long n1 = Long.parseLong(n);
            if (n1 > 0) {
                return n1;
            } else {
                throw new IllegalArgumentException("Error: Number must be greater than 0.");
            }
        } catch (NumberFormatException e) {

            throw new IllegalArgumentException("Error: Number must be double.");
        }
    }

    /**
     * Reads and validates the albums count from the BufferedReader.
     *
     * @param bufferedReader the BufferedReader to read from
     * @return the albums count (must be > 0)
     * @throws IOException                if an I/O error occurs
     * @throws IllegalArgumentException if the input is empty, not a valid long, or ≤ 0
     */
    private long readAlbumsCount(BufferedReader bufferedReader) throws IOException {

        String n = bufferedReader.readLine().trim();

        if (n.isEmpty()) {

            throw new IllegalArgumentException("Error: Number cannot be empty.");
        } else try {
            long n1 = Long.parseLong(n);
            if (n1 > 0) {
                return n1;
            } else {
                throw new IllegalArgumentException("Error: Number must be greater than 0.");
            }
        } catch (NumberFormatException e) {

            throw new IllegalArgumentException("Error: Number must be double.");
        }

    }

    /**
     * Reads and validates the music genre from the BufferedReader.
     *
     * @param bufferedReader the BufferedReader to read from
     * @return the corresponding MusicGenre enum value, or null if the input is empty
     * @throws IOException                if an I/O error occurs
     * @throws IllegalArgumentException if the input is not empty and does not match any genre
     */
    private MusicGenre readMusicGenre(BufferedReader bufferedReader) throws IOException {

        String genre = bufferedReader.readLine().trim();
        if (genre.isEmpty()) {
            return null;
        } else {
            try {
                return MusicGenre.valueOf(genre.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Error: Invalid genre.");
            }
        }

    }

    /**
     * Reads and validates the label bands count from the BufferedReader and constructs a Label object.
     *
     * @param bufferedReader the BufferedReader to read from
     * @return a Label object with the parsed bands count
     * @throws IllegalArgumentException if the input is not a valid integer or an I/O error occurs
     */
    private Label readLabel(BufferedReader bufferedReader) {

        try {
            Integer x = Integer.parseInt(bufferedReader.readLine().trim());
            return new Label(x);
        } catch (IllegalArgumentException | IOException e) {
            throw new IllegalArgumentException("Error: Invalid label.");
        }
    }

}