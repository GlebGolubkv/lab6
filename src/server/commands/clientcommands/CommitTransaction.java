package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;

public class CommitTransaction extends Command {
    @Override
    public Response execute() {

        try {
            ClassesManager.getInstance().commitTransaction();


            StringBuilder stringBuilder = new StringBuilder().append("Committed transaction");

            return new Response(true, "CommitTransaction successfully completed.", stringBuilder);

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
        return "фиксирует транзакцию";
    }
}
