package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;
import common.dataclasses.Colors;

public class BeginTransaction extends Command {
    @Override
    public Response execute() {


        try {
            ClassesManager.getInstance().beginTransaction();

            StringBuilder stringBuilder = new StringBuilder().append("Beginning transaction");


            return new Response(true, "BeginTransaction successfully completed." + Colors.RESET, stringBuilder);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
        return "начать транзакцию";
    }
}
