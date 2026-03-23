package commandsmanager.сommands;

import commandsmanager.Command;
import data.ClassesManager;
import data.KeyGenerator;
import dataclasses.MusicBand;
import termenalmanager.BandsInputTerminal;
import termenalmanager.Colors;

import java.util.Scanner;

public class Insert extends Command {
    @Override
    public void execute() {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void execute(String value1) {
        int key = keyChek(value1);
        Scanner scanner = new Scanner(System.in);
        MusicBand musicBand = BandsInputTerminal.getInstance().inputBand(scanner);
        ClassesManager.getInstance().addMusicBandToCollection(key, musicBand);


    }

    @Override
    public void execute(String value1, MusicBand musicBand){
        int key = keyChek(value1);
        ClassesManager.getInstance().addMusicBandToCollection(key, musicBand);

    }

    @Override
    public void execute(MusicBand value1) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "добавить новый элемент с заданным ключом";
    }


    private int keyChek(String key) {
        int newKey;


        try {
            newKey = Integer.parseInt(key);
        } catch (NumberFormatException e) {
            int newGenerateKey = new KeyGenerator().generateNewKey();
            System.out.println("This key is unavailable. New Key created automatically. Key: " + newGenerateKey);
            return newGenerateKey;
        }

        //если уже есть в массиве
        if (!ClassesManager.getInstance().getMap().containsKey(newKey)) {
            return newKey;
        } else {
            int newGenerateKey = new KeyGenerator().generateNewKey();
            System.out.println("This key is already in Map. New Key created automatically. Key: " + newGenerateKey);
            return newGenerateKey;
        }
    }
}
