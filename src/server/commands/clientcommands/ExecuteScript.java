package server.commands.clientcommands;

import common.dataclasses.MusicBand;
import server.commands.Command;
import server.filemanager.CommandsReader;
import common.Response;

/**
 * Команда чтения и последовательного выполнения команд из файла скрипта.
 */
public class ExecuteScript extends Command {

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(int client_id) {
        throw new IllegalArgumentException("Not supported");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1,int client_id) {

         StringBuilder stringBuilder = CommandsReader.getInstance().readCommands(value1, client_id);

        CommandsReader.getInstance().resetCommand();
        
        

        return new Response(true, "ExecuteScript successfully completed.",  stringBuilder);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1, MusicBand value2,int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(MusicBand value1,int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String commandInfo() {
        return "считать и исполнить скрипт из указанного файла";
    }
}
