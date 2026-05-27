package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import server.data.generators.KeyGenerator;
import common.dataclasses.MusicBand;
import common.Response;
import server.postgres.CommandsDAO;

/**
 * Команда добавления нового элемента коллекции с заданным ключом.
 */
public class Insert extends Command {

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1,int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1, MusicBand musicBand,int client_id) {
        StringBuilder stringBuilder = new StringBuilder();
        int key = keyChek(value1, stringBuilder);

        MusicBand qlMusicBand = CommandsDAO.insertMusicBand(key, musicBand, client_id );
        ClassesManager.getInstance().addMusicBandToCollection(key, qlMusicBand, client_id);

        return new Response(true, "Insert successfully completed.", stringBuilder);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(MusicBand value1,int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
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

        boolean keyTaken = ClassesManager.getInstance().getCollection().containsKey(newKey)
                || CommandsDAO.bandKeyExists(newKey);
        if (!keyTaken) {
            return newKey;
        }
        int newGenerateKey = new KeyGenerator().generateNewKey();
        stringBuilder.append("Key ").append(newKey)
                .append(" is already used. New key created automatically: ")
                .append(newGenerateKey);
        return newGenerateKey;
    }
}
