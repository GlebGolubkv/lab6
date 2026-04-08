package server.commands.clientcommands;


import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;
import common.dataclasses.Colors;

public class Clear extends Command {
    @Override
    public Response execute() {
        ClassesManager.getInstance().clearCollection();

        StringBuilder stringBuilder = new StringBuilder().append(Colors.GREEN + "Collection cleared" + Colors.RESET);

        return new Response(true, "Clear successfully completed.", stringBuilder);

    }

    @Override
    public Response execute(String value1) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(String value1, MusicBand value2) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(MusicBand value1) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "очистить коллекцию";
    }
}
