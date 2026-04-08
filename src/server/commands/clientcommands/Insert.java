package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import server.data.generators.KeyGenerator;
import common.dataclasses.MusicBand;
import common.Response;

public class Insert extends Command {
    @Override
    public Response execute() {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(String value1) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(String value1, MusicBand musicBand) {
        StringBuilder stringBuilder = new StringBuilder();
        int key = keyChek(value1, stringBuilder);
        ClassesManager.getInstance().addMusicBandToCollection(key, musicBand);

        return new Response(true, "Insert successfully completed.", stringBuilder);

    }

    @Override
    public Response execute(MusicBand value1) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "добавить новый элемент с заданным ключом";
    }


    private int keyChek(String key, StringBuilder stringBuilder) {
        int newKey;


        try {
            newKey = Integer.parseInt(key);
        } catch (NumberFormatException e) {
            int newGenerateKey = new KeyGenerator().generateNewKey();
            stringBuilder.append("This key is unavailable. New Key created automatically. Key: " + newGenerateKey);
            return newGenerateKey;
        }

        //если уже есть в массиве
        if (!ClassesManager.getInstance().getMap().containsKey(newKey)) {
            return newKey;
        } else {
            int newGenerateKey = new KeyGenerator().generateNewKey();
            stringBuilder.append("This key is already in Map. New Key created automatically. Key: " + newGenerateKey);
            return newGenerateKey;
        }
    }
}
