package server.commands.internalcommands;

import common.Response;
import common.dataclasses.MusicBand;
import server.commands.Command;
import server.postgres.CommandsDAO;

/**
 * Внутренняя команда регистрации нового пользователя в базе данных.
 */
public class InsertUser extends Command {

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(int clientId) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1, int clientId) {
        String[] values = value1.split("\\s+");
        String username = values[0];
        String password = values[1];

        if (!CommandsDAO.isUserExists(username).isSuccess()) {

            return CommandsDAO.insertUser(username, password);

        } else {
            return new Response(false, "Username is already in use");
        }
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
    public Response execute(MusicBand value1,  int clientId) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String commandInfo() {
        return "добавить пользователя";
    }
}
