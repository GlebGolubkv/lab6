package commandsmanager.сommands;

import commandsmanager.Command;
import data.ClassesManager;
import dataclasses.MusicBand;
import termenalmanager.Colors;

import java.util.Hashtable;

public class FilterLessThanLabel extends Command {
    @Override
    public void execute() {
        throw new IllegalArgumentException("Not supported");
    }


    @Override
    public void execute(String value1) {
        int label = checkInteger(value1);
        int count = 0;
        ClassesManager classesManager = ClassesManager.getInstance();
        Hashtable<Integer, MusicBand> map = classesManager.getMap();

        for (int key : map.keySet()) {
            if (label > map.get(key).getLabel().getBands()) {
                count++;
            }
        }

        System.out.println();
        System.out.println("The number of elements less than " + Colors.GREEN + label + Colors.RESET + " is " + Colors.GREEN + count + Colors.RESET);
        System.out.println();

    }

    @Override
    public void execute(String value1, MusicBand value2) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void execute(MusicBand value1) {
        throw new IllegalArgumentException("Not supported");
    }

    private int checkInteger(String key) {
        int newKey;
        try {
            newKey = Integer.parseInt(key);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Must be an integer");
        }
        return newKey;
    }

    @Override
    public String commandInfo() {
        return "вывести элементы, значение поля label которых меньше заданного";
    }
}
