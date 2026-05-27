package server.data;

import common.dataclasses.MusicBand;
import common.dataclasses.MusicBandEntry;

import server.postgres.CommandsDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Менеджер in-memory коллекции музыкальных групп, синхронизированной с PostgreSQL.
 */
public class ClassesManager {
    private static ClassesManager instance;

    private final Map<Integer, MusicBand> collection = Collections.synchronizedMap(new HashMap<Integer, MusicBand>());
    private final Map<Integer, Integer> ownersByKey = new HashMap<>();

    private ClassesManager() {
        collection.putAll(CommandsDAO.readFromPostgres());
        ownersByKey.putAll(CommandsDAO.readBandKeyOwnerMap());
    }

    /**
     * Возвращает единственный экземпляр менеджера коллекции.
     *
     * @return инициализированный {@link ClassesManager}
     * @throws RuntimeException если менеджер не инициализирован
     */
    public static ClassesManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("ClassesManager not initialized");
        }
        return instance;
    }

    /**
     * Инициализирует менеджер коллекции (загрузка данных из БД).
     *
     * @throws RuntimeException если менеджер уже инициализирован
     */
    public static synchronized void initialize() {

        if (instance == null) {
            instance = new ClassesManager();
        } else {
            throw new RuntimeException("ClassesManager already initialized");
        }

    }

    /**
     * Возвращает текущий размер коллекции.
     *
     * @return количество элементов
     */
    public int mapSize() {
        return getActiveMap().size();
    }

    /**
     * Возвращает строковое описание типа коллекции.
     *
     * @return имя класса коллекции
     */
    public String getCollectionType() {
        return "Collection Type: " + getActiveMap().getClass().getName();
    }

    /**
     * Возвращает синхронизированную карту коллекции (ключ — идентификатор группы в коллекции).
     *
     * @return карта коллекции
     */
    public Map<Integer, MusicBand> getCollection() {
        return getActiveMap();
    }

    /**
     * Добавляет музыкальную группу в коллекцию по ключу.
     *
     * @param key     ключ элемента
     * @param mb      музыкальная группа
     * @param ownerId идентификатор владельца
     */
    public void addMusicBandToCollection(int key, MusicBand mb, int ownerId) {
        synchronized (collection) {
            getActiveMap().put(key, mb);
            ownersByKey.put(key, ownerId);
        }
    }

    /**
     * Удаляет элемент коллекции по ключу.
     *
     * @param key ключ элемента
     */
    public void removeMusicBandFromCollection(int key) {
        synchronized (collection) {
            getActiveMap().remove(key);
            ownersByKey.remove(key);
        }
    }

    /**
     * Формирует список записей коллекции с ключом и владельцем (для GUI).
     *
     * @return снимок коллекции в виде {@link MusicBandEntry}
     */
    public List<MusicBandEntry> getCollectionEntries() {
        List<MusicBandEntry> entries = new ArrayList<>();
        synchronized (collection) {
            for (Map.Entry<Integer, MusicBand> entry : getActiveMap().entrySet()) {
                int key = entry.getKey();
                int ownerId = ownersByKey.getOrDefault(key, 0);
                entries.add(new MusicBandEntry(key, ownerId, entry.getValue()));
            }
        }
        return entries;
    }

    /**
     * Проверяет наличие ключа в коллекции.
     *
     * @param key ключ элемента
     * @return {@code true}, если ключ присутствует
     */
    public boolean keyInMap(int key) {
        return getActiveMap().containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        synchronized (collection) {
            StringBuilder s = new StringBuilder();
            for (Integer key : getActiveMap().keySet()) {
                s.append("Key: ").append(key).append("\n")
                        .append(" Value: ")
                        .append(getActiveMap().get(key).toString()).append("\n");

            }
            return s.toString();
        }
    }

    /**
     * Формирует строковое представление коллекции, отсортированное по имени группы.
     *
     * @return текстовое описание всех элементов
     */
    public String showCollection() {
        synchronized (collection) {
            return getActiveMap().keySet()
                    .stream()
                    .sorted((a, b) -> (getActiveMap().get(b).getName().compareTo(getActiveMap().get(a).getName())))
                    .map(key -> "Key: " + key + "\nValue: " + getActiveMap().get(key).toString() + "\n")
                    .collect(Collectors.joining());
        }

    }

    private Map<Integer, MusicBand> getActiveMap() {
        return collection;
    }

}
