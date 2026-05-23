package client.gui;

import client.ClientInitializer;
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
 * Sends requests to the server off the JavaFX application thread.
 */
public class ClientService {

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r, "gui-client-io");
        t.setDaemon(true);
        return t;
    });

    public ClientService() {
        ClientInitializer.initialize();
    }

    public CompletableFuture<Response> sendAsync(Request request, ClientNetworkManager manager) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return manager.sendRequest(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, EXECUTOR);
    }

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
