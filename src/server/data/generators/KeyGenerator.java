package server.data.generators;

import common.dataclasses.MusicBand;
import server.data.ClassesManager;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

/**
 * Генератор нового свободного ключа для элемента коллекции.
 */
public class KeyGenerator {

    /**
     * Возвращает новый ключ как максимальный существующий ключ плюс один.
     *
     * @return сгенерированный ключ
     */
    public int generateNewKey() {

        Map<Integer, MusicBand> Map =  ClassesManager.getInstance().getCollection();

        return Collections.max(Map.keySet()) + 1;
    }

}
