package commandsmanager.сommands;

import commandsmanager.Command;
import data.ClassesManager;
import dataclasses.MusicBand;
import termenalmanager.Colors;

public class Save extends Command {
    @Override
    public void execute() {
        ClassesManager classesManager = ClassesManager.getInstance();
        classesManager.saveCollectionToFile();

        System.out.println();
        System.out.println(Colors.GREEN + "The collection has been saved" + Colors.RESET);
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
        return "сохранить коллекцию в файл";
    }
}
