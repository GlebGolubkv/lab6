package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;

public class PrintFieldDescendingLabel extends Command {
    @Override
    public Response execute() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Labels: ");


        ClassesManager.getInstance().getMap().keySet().stream()
                .map(e -> ClassesManager.getInstance().getMap().get(e).getLabel().getBands())
                .sorted((a, b) -> b - a)
                .forEach(e -> stringBuilder.append(e).append(" "));


        return new Response(true, "PrintFieldDescendingLabel successfully completed.", stringBuilder);


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
        return "вывести значения поля label всех элементов в порядке убывания";
    }
}
