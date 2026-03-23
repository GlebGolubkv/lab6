package data;


import dataclasses.MusicBand;

import java.util.Collections;
import java.util.Hashtable;


public class KeyGenerator {

    public int generateNewKey() {


        Hashtable<Integer, MusicBand> Map =  ClassesManager.getInstance().getMap();

        return Collections.max(Map.keySet()) + 1;
    }
}
