package commandsmanager.сommands;

import commandsmanager.Command;
import data.ClassesManager;
import dataclasses.MusicBand;
import termenalmanager.Colors;

public class Show extends Command {
    @Override
    public void execute() {

        System.out.println();
        System.out.println(ClassesManager.getInstance());
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
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
