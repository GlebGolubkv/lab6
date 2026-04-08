package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;

import java.util.List;

public class RemoveLower extends Command {
    @Override
    public Response execute() {
        throw new IllegalArgumentException("Not supported");
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

        List<String> collect = ClassesManager.getInstance().getMap().keySet()
                .stream()
                .filter(e -> value1.compareTo(ClassesManager.getInstance().getMap().get(e)) > 0)
                .map(e -> String.valueOf(e))
                .toList();


        StringBuilder stringBuilder = new StringBuilder();

        for (String key : collect) {

            stringBuilder.append(new RemoveKey().execute(key).getData()).append("\n");

        }


        return new Response(true, "RemoveLower successfully completed.", stringBuilder);

    }

    @Override
    public String commandInfo() {
        return "удалить из коллекции все элементы, меньшие, чем заданный";
    }
}
