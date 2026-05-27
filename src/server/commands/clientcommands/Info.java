package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;

/**
 * Команда вывода информации о коллекции (тип и размер).
 */
public class Info extends Command {

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(int client_id) {

        ClassesManager cm = ClassesManager.getInstance();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(cm.getCollectionType() + "\n");
        stringBuilder.append("Map size: ").append(cm.mapSize());

        return new Response(true, "Info successfully completed.", stringBuilder);

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
        return "вывести в стандартный поток вывода информацию о коллекции";
    }
}
