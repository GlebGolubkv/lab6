package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;
import server.postgres.CommandsDAO;

/**
 * Команда замены значения по ключу, если новое значение больше старого.
 */
public class ReplaceIfGreater extends Command {

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

        int key = checkInteger(value1);

        ClassesManager cm = ClassesManager.getInstance();
        MusicBand oldMusicBand = cm.getCollection().get(key);
        if (oldMusicBand == null) {
            return new Response(false, "The replacement object was not found",
                    new StringBuilder().append("Key ").append(key).append(" was not found in the collection."));
        }

        if (value2.compareTo(oldMusicBand) > 0) {
            if (CommandsDAO.updateMusicBandByKey(key, value2, client_id)) {

                cm.getCollection().put(key, value2);
                StringBuilder stringBuilder = new StringBuilder().append("Key ").append(key).append(" replaced");
                return new Response(true, "ReplaceIfLower successfully completed.", stringBuilder);
            } else {
                return new Response(false, "The replacement object was not found");
            }

        }
        return new Response(true, "ReplaceIfLower successfully completed.");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(MusicBand value1, int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String commandInfo() {
        return "заменить значение по ключу, если новое значение больше старого";
    }

    private int checkInteger(String key) {
        int newKey;

        try {
            newKey = Integer.parseInt(key);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Must be an integer");
        }
        return newKey;
    }
}
