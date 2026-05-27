package client;

import common.JsonDataMapper;

/**
 * Инициализатор общих компонентов клиента перед началом работы.
 */
public class ClientInitializer {

    /**
     * Выполняет начальную настройку сериализации JSON.
     */
    public static void initialize() {
        JsonDataMapper.initialize();
    }
}
