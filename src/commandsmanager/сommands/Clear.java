package commandsmanager.сommands;


import commandsmanager.Command;
import data.ClassesManager;
import dataclasses.MusicBand;
import termenalmanager.Colors;

public class Clear extends Command {
    @Override
    public void execute() {
        ClassesManager.getInstance().clearCollection();
        System.out.println();
        System.out.println(Colors.GREEN + "Collection cleared" + Colors.RESET);
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
        return "очистить коллекцию";
    }
}
