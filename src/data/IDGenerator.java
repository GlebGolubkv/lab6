package data;

import dataclasses.MusicBand;

import java.util.Hashtable;


public class IDGenerator {

    public int generateNewID() {

        int maxId = 1;


        Hashtable<Integer, MusicBand> Map = ClassesManager.getInstance().getMap();

        for (MusicBand mb : Map.values()) {
            if (maxId <= mb.getId()) {
                maxId = (mb.getId() + 1);
            }
        }

        return maxId;
    }
}
