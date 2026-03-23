package commandsmanager.сommands;

import commandsmanager.Command;
import data.ClassesManager;
import dataclasses.MusicBand;
import termenalmanager.BandsInputTerminal;
import termenalmanager.Colors;

import java.util.ArrayList;
import java.util.Scanner;

public class RemoveLower extends Command {
    @Override
    public void execute() {

        Scanner scanner = new Scanner(System.in);
        ClassesManager classesManager = ClassesManager.getInstance();
        MusicBand inputMusicBand = BandsInputTerminal.getInstance().inputBand(scanner);
        ArrayList<String> removedKeys = new ArrayList<>();

        for (int key : classesManager.getMap().keySet()) {
            if (inputMusicBand.compareTo(classesManager.getMap().get(key)) > 0) {

                removedKeys.add(String.valueOf(key));

            }
        }


        for (String key : removedKeys) {
            new RemoveKey().execute(key);
        }


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

        ClassesManager classesManager = ClassesManager.getInstance();
        ArrayList<String> removedKeys = new ArrayList<>();

        for (int key : classesManager.getMap().keySet()) {
            if (value1.compareTo(classesManager.getMap().get(key)) > 0) {

                removedKeys.add(String.valueOf(key));

            }

        }
    }

    @Override
    public String commandInfo() {
        return "удалить из коллекции все элементы, меньшие, чем заданный";
    }
}
