package commandsmanager.сommands;

import commandsmanager.Command;
import data.ClassesManager;
import dataclasses.MusicBand;
import termenalmanager.BandsInputTerminal;
import termenalmanager.Colors;

import java.util.Scanner;

public class ReplaceIfGreater extends Command {
    @Override
    public void execute() {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void execute(String value1) {
        int key = checkInteger(value1);
        Scanner scanner = new Scanner(System.in);
        ClassesManager cm = ClassesManager.getInstance();
        MusicBand oldMusicBand = cm.getMap().get(key);
        MusicBand newMusicBand = BandsInputTerminal.getInstance().inputBand(scanner);
        if (newMusicBand.compareTo(oldMusicBand) > 0) {
            cm.getMap().put(key, newMusicBand);
            System.out.println();
            System.out.println("Key " + Colors.GREEN + key + Colors.RESET + " replaced");
            System.out.println();
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
        return "заменить значение по ключу, если новое значение больше старого";
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
