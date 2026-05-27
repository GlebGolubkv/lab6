package server.data;

import common.JsonDataMapper;
import server.filemanager.*;
import server.filemanager.BandsFileReader;
import server.postgres.ConnectionInitializer;

/**
 * Точка инициализации серверных подсистем: БД, JSON, команды, чтение файлов, коллекция.
 */
public class DataInitializer {

    public static DataInitializer instance;

    private DataInitializer() {

        ConnectionInitializer.initialize();
        JsonDataMapper.initialize();
        DataCommands.initialize();
        BandsFileReader.initialize();
        CommandsReader.initialize();
        ClassesManager.initialize();

    }

    /**
     * Возвращает единственный экземпляр инициализатора данных.
     *
     * @return инициализированный {@link DataInitializer}
     * @throws RuntimeException если инициализация не выполнялась
     */
    public static DataInitializer getInstance() {
        if (instance == null) {
            throw new RuntimeException("data has not been initialized");
        }
        return instance;
    }

    /**
     * Выполняет полную инициализацию серверных данных и подсистем.
     *
     * @throws RuntimeException при повторной инициализации
     */
    public static void initialize() {
        if (instance == null) {
            instance = new DataInitializer();
        } else {
            throw new RuntimeException("data has not been initialized");
        }
    }
}
