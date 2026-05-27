package client.gui;

import client.ClientNetworkManager;

/**
 * Данные авторизованной сессии клиента: сетевой менеджер, идентификатор и имя пользователя.
 */
public class Session {

    private final ClientNetworkManager networkManager;
    private final int userId;
    private final String username;

    /**
     * Создаёт сессию после успешного входа или регистрации.
     *
     * @param networkManager активное соединение с сервером
     * @param userId         идентификатор пользователя на сервере
     * @param username       имя пользователя
     */
    public Session(ClientNetworkManager networkManager, int userId, String username) {
        this.networkManager = networkManager;
        this.userId = userId;
        this.username = username;
    }

    /**
     * @return менеджер сетевого взаимодействия с сервером
     */
    public ClientNetworkManager getNetworkManager() {
        return networkManager;
    }

    /**
     * @return идентификатор текущего пользователя
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @return имя текущего пользователя
     */
    public String getUsername() {
        return username;
    }
}
