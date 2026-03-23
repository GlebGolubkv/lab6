package commandsmanager.сommands;

import commandsmanager.Command;
import data.DataCommands;
import dataclasses.MusicBand;
import termenalmanager.Colors;

public class Help extends Command {

    public Help() {
    }


    @Override
    public void execute() {
        StringBuilder helpCommands = new StringBuilder();
        DataCommands data = DataCommands.getInstance();
        for (String name : data.getNames()) {
            helpCommands.append(Colors.WHITE + "Command: " + Colors.GREEN)
                    .append(name).append(" : " + Colors.RESET).append(data.getCommand(name).commandInfo()).append("\n");

        }
        System.out.println();
        System.out.println(Colors.GREEN + "сommands: " + Colors.RESET);
        System.out.println(helpCommands);
        System.out.println();

    }

    @Override
    public void execute(String value1) {

        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void execute(String value1, MusicBand value2) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void execute(MusicBand value1) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "вывести справку по доступным командам";
    }

}
