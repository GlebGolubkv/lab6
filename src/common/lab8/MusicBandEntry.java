package common.lab8;

import common.dataclasses.MusicBand;

/**
 * Lab 8 view-model: one element of the collection with map key and owner id.
 */
public class MusicBandEntry {

    private int bandKey;
    private int ownerId;
    private MusicBand musicBand;

    public MusicBandEntry() {
    }

    public MusicBandEntry(int bandKey, int ownerId, MusicBand musicBand) {
        this.bandKey = bandKey;
        this.ownerId = ownerId;
        this.musicBand = musicBand;
    }

    public int getBandKey() {
        return bandKey;
    }

    public void setBandKey(int bandKey) {
        this.bandKey = bandKey;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public MusicBand getMusicBand() {
        return musicBand;
    }

    public void setMusicBand(MusicBand musicBand) {
        this.musicBand = musicBand;
    }
}
