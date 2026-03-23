package commandsmanager.сommands;

import commandsmanager.Command;
import data.ClassesManager;
import dataclasses.MusicBand;
import termenalmanager.Colors;

public class Info extends Command {
    @Override
    public void execute() {

        ClassesManager cm = ClassesManager.getInstance();
        System.out.println();
        System.out.println(cm.getCollectionType());
        System.out.println(cm.getCreationDate());
        System.out.println(Colors.GREEN + "Map size: " + Colors.RESET + cm.mapSize());
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
        return "вывести в стандартный поток вывода информацию о коллекции";
    }
}
