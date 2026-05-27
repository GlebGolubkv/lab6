package common.dataclasses;

/**
 * Запись коллекции: ключ элемента, идентификатор владельца и данные {@link MusicBand}.
 */
public class MusicBandEntry {

    private int bandKey;
    private int ownerId;
    private MusicBand musicBand;

    /**
     * Конструктор по умолчанию для десериализации JSON.
     */
    public MusicBandEntry() {
    }

    /**
     * Создаёт запись коллекции с ключом, владельцем и данными группы.
     *
     * @param bandKey   ключ элемента в коллекции
     * @param ownerId   идентификатор пользователя-владельца
     * @param musicBand данные музыкальной группы
     */
    public MusicBandEntry(int bandKey, int ownerId, MusicBand musicBand) {
        this.bandKey = bandKey;
        this.ownerId = ownerId;
        this.musicBand = musicBand;
    }

    /**
     * @return ключ элемента в коллекции
     */
    public int getBandKey() {
        return bandKey;
    }

    /**
     * @param bandKey ключ элемента в коллекции
     */
    public void setBandKey(int bandKey) {
        this.bandKey = bandKey;
    }

    /**
     * @return идентификатор владельца записи
     */
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId идентификатор владельца записи
     */
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * @return данные музыкальной группы
     */
    public MusicBand getMusicBand() {
        return musicBand;
    }

    /**
     * @param musicBand данные музыкальной группы
     */
    public void setMusicBand(MusicBand musicBand) {
        this.musicBand = musicBand;
    }
}
