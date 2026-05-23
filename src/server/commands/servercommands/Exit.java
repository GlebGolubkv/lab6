package server.commands.servercommands;

import common.Response;
import common.dataclasses.Colors;
import common.dataclasses.MusicBand;
import server.commands.Command;

public class Exit extends Command {
    @Override
    public Response execute(int clientId) {

        System.exit(0);

        return new  Response(true, "Exit Program");
    }

    @Override
    public Response execute(String value1,int clientId) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(String value1, MusicBand value2,int clientId) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(MusicBand value1,int clientId) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "завершить программу";
    }
}