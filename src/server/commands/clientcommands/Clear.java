package server.commands.clientcommands;


import server.commands.Command;

import common.dataclasses.MusicBand;
import common.Response;

import server.postgres.CommandsDAO;


public class Clear extends Command {
    @Override
    public Response execute(int client_id) {
        try {
            String string = CommandsDAO.clearMusicBands(client_id).toString();

            return new Response(true, string);
        } catch (Exception e) {
           throw new RuntimeException(e.getMessage());
        }


    }

    @Override
    public Response execute(String value1, int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(String value1, MusicBand value2, int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(MusicBand value1, int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "очистить коллекцию";
    }
}
