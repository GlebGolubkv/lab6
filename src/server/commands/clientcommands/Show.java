package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;

/**
 * Команда вывода всех элементов коллекции в строковом представлении.
 */
public class Show extends Command {

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(int client_id) {

        StringBuilder stringBuilder = new StringBuilder().append(ClassesManager.getInstance().showCollection());

        return new Response(true, "Show successfully completed.", stringBuilder);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1,int client_id) {

        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1, MusicBand value2,int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(MusicBand value1,int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String commandInfo() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
