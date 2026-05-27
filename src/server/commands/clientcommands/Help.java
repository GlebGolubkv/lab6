package server.commands.clientcommands;

import server.commands.Command;
import server.data.DataCommands;
import common.dataclasses.MusicBand;
import common.Response;

/**
 * Команда вывода справки по доступным клиентским командам.
 */
public class Help extends Command {

    /**
     * Создаёт команду справки.
     */
    public Help() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(int client_id) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Commands:\n");

        DataCommands.getInstance().getCommands().stream().filter(e -> !e.isInternalOnly())
                .sorted((a, b) -> (b.getCommandName().length() - a.getCommandName().length()))
                .forEach(name -> stringBuilder.append("Command: "
                + name.getCommandName() +
                " : "
                + name.getDescription() + "\n"));

        return new Response(true, "Help successfully completed.", stringBuilder);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1,int client_id) {

        throw new IllegalArgumentException("Not supported");
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
        return "вывести справку по доступным командам";
    }

}
