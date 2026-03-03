package DataClasses;

import TernemalManager.Colors;

import java.time.ZonedDateTime;
import java.io.Serializable;

/**
 * Класс, который описывает объект типа MusicBand
 */
public class MusicBand implements Comparable<MusicBand> {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long numberOfParticipants; //Значение поля должно быть больше 0
    private long albumsCount; //Значение поля должно быть больше 0
    private MusicGenre genre; //Поле может быть null
    private Label label; //Поле не может быть null

    public MusicBand() {
    }

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


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public long getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public long getAlbumsCount() {
        return albumsCount;
    }

    public MusicGenre getGenre() {
        return genre;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    public void setAlbumsCount(long albumsCount) {
        this.albumsCount = albumsCount;
    }

    public void setNumberOfParticipants(long numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MusicBand{" + Colors.WHITE + "id= " + Colors.RESET + id + Colors.WHITE + ", name= " + Colors.RESET + name + Colors.WHITE + ", " + Colors.RESET + coordinates + Colors.WHITE +
                ", creationDate= " + Colors.RESET + creationDate + Colors.WHITE + ", numberOfParticipants= " + Colors.RESET
                + numberOfParticipants + Colors.WHITE + ", albumsCount= " + Colors.RESET + albumsCount + Colors.WHITE + ", genre= " + Colors.RESET + genre + Colors.WHITE + ", " + Colors.RESET + label + Colors.WHITE + '}' + Colors.RESET;
    }


    /**
     * Объекты сравниваются по 2 полям. Сначала по Label, далее по ID
     * @param o
     * @return 1 в случае, если текущий объект больше. 0, если текущий объект равен и -1, если текущий объект меньше.
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






