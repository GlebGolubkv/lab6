package CommandsManager.Commands;

import CommandsManager.Command;
import Data.ClassesManager;
import Data.KeyGenerator;
import DataClasses.MusicBand;
import TernemalManager.BandsInputManager;

import java.util.Scanner;

public class Insert extends Command {
    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void execute(String value1) {
        ClassesManager classesManager = new ClassesManager();
        int key = keyChek(value1);
        Scanner scanner = new Scanner(System.in);
        MusicBand musicBand = new BandsInputManager().InputBand(scanner);
        classesManager.addMusicBandToCollection(key, musicBand);


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
        if (!ClassesManager.Map.containsKey(newKey)) {
            return newKey;
        } else {
            int newGenerateKey = new KeyGenerator().generateNewKey();
            System.out.println("This key is already in Map. New Key created automatically. Key: " + newGenerateKey);
            return newGenerateKey;
        }
    }
}
