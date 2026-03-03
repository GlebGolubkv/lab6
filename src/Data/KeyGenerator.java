package Data;


import DataClasses.MusicBand;

import java.util.Collections;
import java.util.Hashtable;

/**
 * Класс генерирует ключи
 * <p>
 *     Он создает ключ больший на 1, чем максимальный
 * </p>
 */
public class KeyGenerator {

    public int generateNewKey() {


        Hashtable<Integer, MusicBand> Map =  ClassesManager.getCopyOfMap();

        return Collections.max(Map.keySet()) + 1;
    }
}
