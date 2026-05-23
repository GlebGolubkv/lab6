package server.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionInitializer {

    private static ConnectionInitializer instance;
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "glebgolubkov";
    private final String password = "";


    private ConnectionInitializer() {
    }


    public static synchronized void initialize() {
        if (instance == null) {
            instance = new ConnectionInitializer();
        } else {
            throw new IllegalStateException("Already initialized");
        }
    }


    public static ConnectionInitializer getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Not initialized. Call initialize() first.");
        }
        return instance;
    }

    // Получить соединение с БД
    public  Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}