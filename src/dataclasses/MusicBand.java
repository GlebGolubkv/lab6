package dataclasses;

import termenalmanager.Colors;

import java.time.ZonedDateTime;

/**
 * Represents a music band entity with various attributes such as name, coordinates,
 * creation date, number of participants, albums count, music genre, and label.
 * Implements {@link Comparable} to allow ordering based on albums count, label bands, and participants count.
 */
public class MusicBand implements Comparable<MusicBand> {
    /**
     * Unique identifier of the music band. Must be positive and generated automatically.
     * Cannot be null.
     */
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    /**
     * Name of the music band. Cannot be null or empty.
     */
    private String name; //Поле не может быть null, Строка не может быть пустой

    /**
     * Coordinates of the music band. Cannot be null.
     */
    private Coordinates coordinates; //Поле не может быть null

    /**
     * Creation date and time of the music band. Generated automatically and cannot be null.
     */
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    /**
     * Number of participants. Must be greater than 0.
     */
    private long numberOfParticipants; //Значение поля должно быть больше 0

    /**
     * Number of albums. Must be greater than 0.
     */
    private long albumsCount; //Значение поля должно быть больше 0

    /**
     * Music genre of the band. Can be null.
     */
    private MusicGenre genre; //Поле может быть null

    /**
     * Label associated with the band. Cannot be null.
     */
    private Label label; //Поле не может быть null

    /**
     * Default constructor for MusicBand.
     */
    public MusicBand() {
    }

    /**
     * Constructs a MusicBand with all required fields, performing validation checks.
     *
     * @param id                    the unique identifier, must be positive and not null
     * @param name                  the band name, must not be null or empty
     * @param coordinates           the coordinates, must not be null
     * @param creationDate          the creation date, must not be null
     * @param numberOfParticipants  the number of participants, must not be negative
     * @param albumsCount           the number of albums, must not be negative
     * @param genre                 the music genre, may be null
     * @param label                 the label, must not be null
     * @throws IllegalArgumentException if any validation constraint is violated
     */
    public MusicBand(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, long numberOfParticipants, long albumsCount, MusicGenre genre, Label label) {
        if (id == null || id <= 0) { // уникальное и генерироваиттся автоматически
            throw new IllegalArgumentException("id is null or negative");
        }
        if (name == null || name.length() < 1) {
            throw new IllegalArgumentException("name is null or empty");
        }
        if (coordinates == null) {
            throw new IllegalArgumentException("coordinates is null");
        }
        if (creationDate == null) { // должно генериться автоматически
            throw new IllegalArgumentException("creationDate is null");
        }
        if (numberOfParticipants < 0) {
            throw new IllegalArgumentException("numberOfParticipants is negative");
        }
        if (albumsCount < 0) {
            throw new IllegalArgumentException("albumsCount is negative");
        }
        if (label == null) {
            throw new IllegalArgumentException("label is null");
        }
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.numberOfParticipants = numberOfParticipants;
        this.albumsCount = albumsCount;
        this.genre = genre;
        this.label = label;
    }

    /**
     * Returns the unique identifier of the band.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the name of the band.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the coordinates of the band.
     *
     * @return the coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Returns the creation date of the band.
     *
     * @return the creation date
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Returns the number of participants.
     *
     * @return the number of participants
     */
    public long getNumberOfParticipants() {
        return numberOfParticipants;
    }

    /**
     * Returns the number of albums.
     *
     * @return the albums count
     */
    public long getAlbumsCount() {
        return albumsCount;
    }

    /**
     * Returns the music genre of the band.
     *
     * @return the genre, may be null
     */
    public MusicGenre getGenre() {
        return genre;
    }

    /**
     * Returns the label associated with the band.
     *
     * @return the label
     */
    public Label getLabel() {
        return label;
    }

    /**
     * Sets the label for the band.
     *
     * @param label the new label
     */
    public void setLabel(Label label) {
        this.label = label;
    }

    /**
     * Sets the music genre for the band.
     *
     * @param genre the new genre
     */
    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    /**
     * Sets the number of albums.
     *
     * @param albumsCount the new albums count
     */
    public void setAlbumsCount(long albumsCount) {
        this.albumsCount = albumsCount;
    }

    /**
     * Sets the number of participants.
     *
     * @param numberOfParticipants the new number of participants
     */
    public void setNumberOfParticipants(long numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    /**
     * Sets the creation date.
     *
     * @param creationDate the new creation date
     */
    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Sets the coordinates.
     *
     * @param coordinates the new coordinates
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Sets the name of the band.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the unique identifier.
     *
     * @param id the new id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns a string representation of the MusicBand object with color formatting.
     *
     * @return a colored string containing all band attributes
     */
    @Override
    public String toString() {
        return "MusicBand{" + Colors.WHITE + "id= " + Colors.RESET + id + Colors.WHITE + ", name= " + Colors.RESET + name + Colors.WHITE + ", " + Colors.RESET + coordinates + Colors.WHITE +
                ", creationDate= " + Colors.RESET + creationDate + Colors.WHITE + ", numberOfParticipants= " + Colors.RESET
                + numberOfParticipants + Colors.WHITE + ", albumsCount= " + Colors.RESET + albumsCount + Colors.WHITE + ", genre= " + Colors.RESET + genre + Colors.WHITE + ", " + Colors.RESET + label + Colors.WHITE + '}' + Colors.RESET;
    }

    /**
     * Compares this MusicBand with another MusicBand.
     * Ordering is determined first by albums count, then by label bands count, and finally by number of participants.
     *
     * @param o the other MusicBand to compare to
     * @return a negative integer, zero, or a positive integer as this band is less than, equal to, or greater than the specified band
     */
    // сравнение по количеству альбомов, далее по количеству Labels, далее по ID
    @Override
    public int compareTo(MusicBand o) {

        int compareAlbums = Long.compare(albumsCount, o.albumsCount);
        if (compareAlbums != 0) {
            return compareAlbums;
        }

        int compareLabels = Integer.compare(label.getBands(), o.label.getBands());
        if (compareLabels != 0) {
            return compareLabels;
        }

        return Long.compare(numberOfParticipants, o.numberOfParticipants);

    }
}