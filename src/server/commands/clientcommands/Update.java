package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;

import java.util.Hashtable;

public class Update extends Command {


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
        StringBuilder stringBuilder = new StringBuilder();

        int key = RemoveBandByID(CheckInteger(value1), stringBuilder);
        ClassesManager.getInstance().getMap().put(key, value2);
        return new Response(true, "Update  successfully completed.", stringBuilder);

    }

    @Override
    public Response execute(MusicBand value1) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }


    public int RemoveBandByID(int ID, StringBuilder stringBuilder) {
        Hashtable<Integer, MusicBand> map = ClassesManager.getInstance().getMap();

        for (int key : map.keySet()) {
            if (ID == map.get(key).getId()) {
                stringBuilder.append("The object that you are replacing\n");
                stringBuilder.append(map.get(key));
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
