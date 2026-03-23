package commandsmanager.сommands;

import commandsmanager.Command;
import dataclasses.MusicBand;
import termenalmanager.Colors;


public class Exit extends Command {
    @Override
    public void execute() {
        System.out.println(Colors.GREEN + "Exit Program" + Colors.RESET);
        System.exit(0);
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
        return "завершить программу";
    }
}
