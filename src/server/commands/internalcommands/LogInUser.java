package server.commands.internalcommands;

import common.Response;

import common.dataclasses.MusicBand;
import server.commands.Command;

import server.postgres.CommandsDAO;


public class LogInUser extends Command {


    @Override
    public Response execute(int clintId) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(String value1, int clintId) {

        String[] values = value1.split("\\s+");
        String username = values[0];
        String password = values[1];

        if (CommandsDAO.isUserExists(username).isSuccess()) {

            return CommandsDAO.logInUser(username, password);

        } else return new Response(false, "Username is invalid");
    }

    @Override
    public Response execute(String value1, MusicBand value2, int clintId) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(MusicBand value1, int clintId) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "войти в аккаунт пользователя";
    }
}
