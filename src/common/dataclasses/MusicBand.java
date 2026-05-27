package common.dataclasses;

import java.time.ZonedDateTime;

/**
 * Музыкальная группа — основной элемент коллекции.
 * Сравнивается по числу альбомов, затем по метке и числу участников.
 */
public class MusicBand implements Comparable<MusicBand> {

    private Integer id;

    private String name;

    private Coordinates coordinates;

    private java.time.ZonedDateTime creationDate;

    private long numberOfParticipants;

    private long albumsCount;

    private MusicGenre genre;

    private Label label;

    /**
     * Конструктор по умолчанию для десериализации JSON.
     */
    public MusicBand() {
    }

    /**
     * Создаёт группу с проверкой обязательных полей и неотрицательных счётчиков.
     *
     * @param id                    идентификатор (не {@code null}, не отрицательный)
     * @param name                  название (не пустое)
     * @param coordinates           координаты (не {@code null})
     * @param creationDate          дата создания (не {@code null})
     * @param numberOfParticipants  число участников (не отрицательное)
     * @param albumsCount           число альбомов (не отрицательное)
     * @param genre                 жанр (может быть {@code null})
     * @param label                 метка (не {@code null})
     * @throws IllegalArgumentException при нарушении ограничений
     */
    public MusicBand(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate,
                     long numberOfParticipants, long albumsCount, MusicGenre genre, Label label) {

        if (id == null || id < 0) {
            throw new IllegalArgumentException("id is null or negative");
        }
        if (name == null || name.length() < 1) {
            throw new IllegalArgumentException("name is null or empty");
        }
        if (coordinates == null) {
            throw new IllegalArgumentException("coordinates is null");
        }
        if (creationDate == null) {
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
        return "MusicBand{id=" + id + ", name=" + name + ", " + coordinates
                + ", creationDate=" + creationDate + ", numberOfParticipants=" + numberOfParticipants
                + ", albumsCount=" + albumsCount + ", genre=" + genre + ", " + label + '}';
    }

    /**
     * Сравнивает группы: сначала по {@code albumsCount}, затем по {@code label.bands},
     * затем по {@code numberOfParticipants}.
     *
     * @param o другая группа
     * @return отрицательное, ноль или положительное число по контракту {@link Comparable}
     */
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
