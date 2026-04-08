package server.commands.clientcommands;

import server.commands.Command;
import server.data.DataCommands;
import common.dataclasses.MusicBand;
import common.Response;
import common.dataclasses.Colors;

public class Help extends Command {

    public Help() {
    }


    @Override
    public Response execute() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(Colors.GREEN + "Commands: " + Colors.RESET + "\n");

        DataCommands.getInstance().getCommands().stream()
                .sorted((a, b) -> (b.getCommandName().length() - a.getCommandName().length()))
                .forEach(name -> stringBuilder.append(Colors.WHITE + "Command: " + Colors.GREEN
                + name.getCommandName() +
                " : " + Colors.RESET
                + name.getDescription() + "\n"));


        return new Response(true, " Help successfully completed.",  stringBuilder);


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
        return "вывести справку по доступным командам";
    }

}
