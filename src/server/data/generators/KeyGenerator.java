package server.data.generators;


import common.dataclasses.MusicBand;
import server.data.ClassesManager;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;


public class KeyGenerator {

    public int generateNewKey() {


        Map<Integer, MusicBand> Map =  ClassesManager.getInstance().getCollection();

        return Collections.max(Map.keySet()) + 1;
    }


}
