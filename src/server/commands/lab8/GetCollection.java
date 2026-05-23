package server.commands.lab8;

import common.Response;
import common.dataclasses.MusicBand;
import common.lab8.MusicBandEntry;
import server.commands.Command;
import server.data.ClassesManager;
import server.postgres.CommandsDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Lab 8 command: returns the in-memory collection with band keys and owner ids.
 */
public class GetCollection extends Command {

    @Override
    public Response execute(int clientId) {
        if (clientId <= 0) {
            return new Response(false, "Authorization required");
        }
        Map<Integer, MusicBand> collection = ClassesManager.getInstance().getCollection();
        List<MusicBandEntry> entries = new ArrayList<>();
        synchronized (collection) {
            for (Map.Entry<Integer, MusicBand> entry : collection.entrySet()) {
                int key = entry.getKey();
                int ownerId = CommandsDAO.findOwnerIdFromBandKey(key);
                entries.add(new MusicBandEntry(key, ownerId, entry.getValue()));
            }
        }
        return new Response(true, "Collection loaded", entries);
    }

    @Override
    public Response execute(String value1, int clientId) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(String value1, MusicBand value2, int clientId) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(MusicBand value1, int clientId) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "получить коллекцию с метаданными для GUI";
    }
}
