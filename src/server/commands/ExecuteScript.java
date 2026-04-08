package server.commands;

import common.dataclasses.MusicBand;
import server.filemanager.CommandsReader;
import common.Response;

public class ExecuteScript extends Command {
    @Override
    public Response execute() {
        throw new IllegalArgumentException("Not supported");

    }

    @Override
    public Response execute(String value1) {

         StringBuilder stringBuilder = CommandsReader.getInstance().readCommands(value1);

        CommandsReader.getInstance().resetCommand();
        // после обнаружения рекурсии - рекурсивно отчищает несколько
        // раз список с командами, так как в каждом запросе идет посл выполнения внутренней рекурсии

        return new Response(true, "ExecuteScript successfully completed.",  stringBuilder);


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
        return "считать и исполнить скрипт из указанного файла";
    }
}
