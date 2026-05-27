package server.commands.internalcommands;

import common.Response;
import common.dataclasses.MusicBand;
import server.commands.Command;
import server.data.ClassesManager;

/**
 * Команда получения коллекции с метаданными (ключ, владелец) для GUI-клиента.
 */
public class GetCollection extends Command {

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(int clientId) {
        if (clientId <= 0) {
            return new Response(false, "Authorization required");
        }
        return new Response(true, "Collection loaded", ClassesManager.getInstance().getCollectionEntries());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1, int clientId) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1, MusicBand value2, int clientId) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(MusicBand value1, int clientId) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String commandInfo() {
        return "получить коллекцию с метаданными для GUI";
    }
}
