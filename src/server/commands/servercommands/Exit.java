package server.commands.servercommands;

import common.Response;
import common.dataclasses.MusicBand;
import server.commands.Command;

/**
 * Серверная команда завершения работы программы.
 */
public class Exit extends Command {

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(int clientId) {

        System.exit(0);

        return new  Response(true, "Exit Program");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1,int clientId) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1, MusicBand value2,int clientId) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(MusicBand value1,int clientId) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String commandInfo() {
        return "завершить программу";
    }
}
