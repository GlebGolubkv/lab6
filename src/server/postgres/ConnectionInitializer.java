package server.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Инициализация и выдача JDBC-соединений с PostgreSQL.
 */
public class ConnectionInitializer {

    private static ConnectionInitializer instance;
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "glebgolubkov";
    private final String password = "";

    private ConnectionInitializer() {
    }

    /**
     * Инициализирует подключение к БД (выполняется один раз).
     *
     * @throws IllegalStateException при повторной инициализации
     */
    public static synchronized void initialize() {
        if (instance == null) {
            instance = new ConnectionInitializer();
        } else {
            throw new IllegalStateException("Already initialized");
        }
    }

    /**
     * Возвращает единственный экземпляр инициализатора соединений.
     *
     * @return инициализированный {@link ConnectionInitializer}
     * @throws IllegalStateException если {@link #initialize()} не вызывался
     */
    public static ConnectionInitializer getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Not initialized. Call initialize() first.");
        }
        return instance;
    }

    /**
     * Открывает новое соединение с базой данных.
     *
     * @return JDBC-соединение
     * @throws SQLException при ошибке подключения
     */
    public  Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
