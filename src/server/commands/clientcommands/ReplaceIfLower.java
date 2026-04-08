package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;
import common.dataclasses.Colors;

import java.util.Scanner;

public class ReplaceIfLower extends Command {
    @Override
    public Response execute() {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(String value1) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(String value1, MusicBand value2) {

        int key = checkInteger(value1);
        Scanner scanner = new Scanner(System.in);
        ClassesManager cm = ClassesManager.getInstance();
        MusicBand oldMusicBand = cm.getMap().get(key);
        if (oldMusicBand == null) {
            throw new IllegalArgumentException("The replacement object was not found");
        }

        if (value2.compareTo(oldMusicBand) < 0) {
            StringBuilder stringBuilder = new StringBuilder().append("Key " + Colors.GREEN + key + Colors.RESET + " replaced");
            cm.getMap().put(key, value2);

            return new Response(true, "ReplaceIfLower successfully completed.", stringBuilder);
        }
        return new Response(true, "ReplaceIfLower successfully completed.");

    }

    @Override
    public Response execute(MusicBand value1) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "заменить значение по ключу, если новое значение меньше старого";
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
