package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;
import common.dataclasses.Colors;

public class Info extends Command {
    @Override
    public Response execute() {

        ClassesManager cm = ClassesManager.getInstance();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(cm.getCollectionType() + "\n");
        stringBuilder.append(cm.getCreationDate() + "\n");
        stringBuilder.append(Colors.GREEN + "Map size: " + Colors.RESET + cm.mapSize());


        return new Response(true, "Info successfully completed.", stringBuilder);


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
        return "вывести в стандартный поток вывода информацию о коллекции";
    }
}
