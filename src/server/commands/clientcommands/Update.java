package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;
import server.postgres.CommandsDAO;

import java.util.Map;

/**
 * Команда обновления элемента коллекции по идентификатору объекта.
 */
public class Update extends Command {

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
    public Response execute(String value1, int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1, MusicBand value2, int client_id) {
        Map<Integer, MusicBand> collection = ClassesManager.getInstance().getCollection();
        StringBuilder stringBuilder = new StringBuilder();
        int id = CheckInteger(value1);
        if (CommandsDAO.updateMusicBandById(id, value2, client_id)) {
            synchronized (collection) {
                int key = removeBandByID(id, stringBuilder);
                collection.put(key, value2);
            }
            return new Response(true, "Update  successfully completed.", stringBuilder);

        }
        return new Response(false, "Update failed.There is no object with this ID. ID: " + id, stringBuilder);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(MusicBand value1, int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String commandInfo() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }

    /**
     * Удаляет элемент коллекции по идентификатору объекта и возвращает его ключ.
     *
     * @param ID            идентификатор музыкальной группы
     * @param stringBuilder буфер для сообщения о заменяемом объекте
     * @return ключ удалённого элемента
     */
    public int removeBandByID(int ID, StringBuilder stringBuilder) {
        Map<Integer, MusicBand> map = ClassesManager.getInstance().getCollection();

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
