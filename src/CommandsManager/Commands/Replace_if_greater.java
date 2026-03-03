package CommandsManager.Commands;

import CommandsManager.Command;
import Data.ClassesManager;
import DataClasses.MusicBand;
import TernemalManager.BandsInputManager;
import TernemalManager.Colors;

import java.util.Scanner;

public class Replace_if_greater extends Command {
    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported");

    }

    @Override
    public void execute(String value1) {
        int key = CheckInteger(value1);
        Scanner scanner = new Scanner(System.in);
        ClassesManager cm = new ClassesManager();
        MusicBand OldMusicBand = cm.GetMap().get(key);
        MusicBand NewMusicBand = new BandsInputManager().InputBand(scanner);
        if (NewMusicBand.compareTo(OldMusicBand) > 0) {
            cm.GetMap().put(key, NewMusicBand);
            System.out.println();
            System.out.println("Key " + Colors.GREEN + key + Colors.RESET + " replaced");
            System.out.println();
        }




    }

    @Override
    public String commandInfo() {
        return "заменить значение по ключу, если новое значение больше старого";
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
