package client.gui;

import client.ClientNetworkManager;
import common.Request;
import common.Response;
import common.dataclasses.CommandType;
import common.dataclasses.MusicBand;
import common.dataclasses.MusicBandEntry;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Асинхронная отправка запросов на сервер из потока GUI (без блокировки интерфейса).
 */
public class ClientService {

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r, "gui-client-io");
        t.setDaemon(true);
        return t;
    });

    /**
     * Создаёт службу асинхронных запросов к серверу для GUI-клиента.
     */
    public ClientService() {

    }



    /**
     * Выполняет вход или регистрацию и при успехе возвращает {@link Session}.
     *
     * @param host     адрес сервера
     * @param port     порт
     * @param username имя пользователя
     * @param password пароль
     * @param register {@code true} — регистрация, {@code false} — вход
     * @return сессия или исключение при ошибке
     */
    public CompletableFuture<Session> authenticate(String host, int port, String username, String password, boolean register) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ClientNetworkManager manager = new ClientNetworkManager(host, port);
                String args = username + " " + password;
                CommandType type = register ? CommandType.INSERT_USER : CommandType.LOG_IN_USER;
                Request request = new Request(type, args, null, 0);
                Response response = manager.sendRequest(request);
                if (!response.isSuccess()) {
                    manager.close();
                    throw new IllegalStateException(response.getMessage());
                }
                return new Session(manager, response.getServerData(), username);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, EXECUTOR);
    }

    /**
     * Загружает коллекцию музыкальных групп текущего пользователя.
     *
     * @param session активная сессия
     * @return список записей коллекции
     */
    public CompletableFuture<List<MusicBandEntry>> fetchCollection(Session session) {
        Request request = new Request(CommandType.GET_COLLECTION, null, null, session.getUserId());
        return CompletableFuture.supplyAsync(() -> {
            try {
                Response response = session.getNetworkManager().sendRequest(request);
                if (!response.isSuccess() || response.getEntries() == null) {
                    throw new IllegalStateException(response.getMessage() != null ? response.getMessage() : "No data");
                }
                return response.getEntries();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, EXECUTOR);
    }

    /**
     * Выполняет команду сервера от имени текущего пользователя.
     *
     * @param session   активная сессия
     * @param type      тип команды
     * @param argument  строковый аргумент команды
     * @param band      объект группы (для команд с телом) или {@code null}
     * @return ответ сервера
     */
    public CompletableFuture<Response> runCommand(Session session, CommandType type, String argument, MusicBand band) {
        Request request = new Request(type, argument, band, session.getUserId());
        return CompletableFuture.supplyAsync(() -> {
            try {
                return session.getNetworkManager().sendRequest(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, EXECUTOR);
    }
}
