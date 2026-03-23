package commandsmanager.сommands;

import commandsmanager.Command;
import data.ClassesManager;

import dataclasses.MusicBand;
import termenalmanager.Colors;


public class RemoveKey extends Command {
    @Override
    public void execute() {
        throw new IllegalArgumentException("Not supported");

    }

    @Override
    public void execute(String value1) {
        int key = checkInteger(value1);
        ClassesManager cm = ClassesManager.getInstance();
        if (cm.keyInMap(key)) {
            cm.removeMusicBandFromCollection(key);
            System.out.println();
            System.out.println("Key " + Colors.GREEN + key + Colors.RESET + " removed");
            System.out.println();
        } else {

            throw new IllegalArgumentException("Invalid key. Key: " + key);

        }


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
        return "удалить элемент из коллекции по его ключу";
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
}
