package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;

import java.util.List;
import java.util.Map;

/**
 * Команда удаления из коллекции всех элементов, меньших заданного.
 */
public class RemoveLower extends Command {

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1, int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1, MusicBand value2, int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(MusicBand value1, int client_id) {

        Map<Integer, MusicBand> collection = ClassesManager.getInstance().getCollection();
        List<String> collect;

        synchronized (collection) {
            collect = collection.keySet()
                    .stream()
                    .filter(e -> value1.compareTo(ClassesManager.getInstance().getCollection().get(e)) > 0)
                    .map(String::valueOf)
                    .toList();
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String key : collect) {
            stringBuilder.append(new RemoveKey().execute(key, client_id).getData()).append("\n");
        }

        return new Response(true, "RemoveLower successfully completed.", stringBuilder);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String commandInfo() {
        return "удалить из коллекции все элементы, меньшие, чем заданный";
    }
}
