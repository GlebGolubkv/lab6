package Data;

import DataClasses.MusicBand;

import java.util.Hashtable;

/**
 * Класс генерирует ID
 * <p>
 *     Он создает ID больший на 1, чем максимальный
 * </p>
 */
public class IDGenerator {

    public int generateNewID() {

        int maxId = 1;


        Hashtable<Integer, MusicBand> Map = ClassesManager.getCopyOfMap();

        for (MusicBand mb : Map.values()) {
            if (maxId <= mb.getId()) {
                maxId = (mb.getId() + 1);
            }
        }

        return maxId;
    }
}
