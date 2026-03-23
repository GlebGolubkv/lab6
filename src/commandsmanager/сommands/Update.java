package commandsmanager.сommands;

import commandsmanager.Command;
import data.ClassesManager;
import dataclasses.MusicBand;
import termenalmanager.BandsInputTerminal;
import termenalmanager.Colors;

import java.util.Hashtable;
import java.util.Scanner;

public class Update extends Command {


    @Override
    public void execute() {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void execute(String value1) {
        Scanner scanner = new Scanner(System.in);

        int key = RemoveBandByID(CheckInteger(value1));
        ClassesManager.getInstance().getMap().put(key, BandsInputTerminal.getInstance().inputBand(CheckInteger(value1), scanner));

    }

    @Override
    public void execute(String value1, MusicBand value2) {

        int key = RemoveBandByID(CheckInteger(value1));
        ClassesManager.getInstance().getMap().put(key, value2);

    }

    @Override
    public void execute(MusicBand value1) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }


    public int RemoveBandByID(int ID) {
        Hashtable<Integer, MusicBand> map = ClassesManager.getInstance().getMap();

        for (int key : map.keySet()) {
            if (ID == map.get(key).getId()) {
                System.out.println("The object that you are replacing");
                System.out.println(map.get(key));
                map.remove(key);
                return key;
            }
        }
        throw new IllegalArgumentException("There is no object with this ID. ID: " + ID);
    }

    private int CheckInteger(String key) {
        int newKey;
        try {
            newKey = Integer.parseInt(key);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Must be an integer");
        }
        return newKey;
    }
}
