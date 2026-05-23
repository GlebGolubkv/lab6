package client.gui;

import client.ClientNetworkManager;

/**
 * Lab 8 client session state after successful authentication.
 */
public class Session {

    private final ClientNetworkManager networkManager;
    private final int userId;
    private final String username;

    public Session(ClientNetworkManager networkManager, int userId, String username) {
        this.networkManager = networkManager;
        this.userId = userId;
        this.username = username;
    }

    public ClientNetworkManager getNetworkManager() {
        return networkManager;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
