package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;

public class Show extends Command {
    @Override
    public Response execute() {

        StringBuilder stringBuilder = new StringBuilder().append(ClassesManager.getInstance().showCollection());

        return new Response(true, "Show successfully completed.", stringBuilder);

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
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
