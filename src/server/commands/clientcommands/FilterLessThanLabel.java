package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;

/**
 * Команда подсчёта элементов, у которых поле label меньше заданного значения.
 */
public class FilterLessThanLabel extends Command {

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
        int label = checkInteger(value1);
        ClassesManager classesManager = ClassesManager.getInstance();
        long result;
        synchronized (classesManager) {
            result = classesManager.getCollection().keySet().stream()
                    
                    .map(e -> classesManager.getCollection().get(e).getLabel().getBands())
                    .filter(e -> e < label)
                    .count();

        }

        StringBuilder stringBuilder = new StringBuilder()
                .append("The number of elements less than ")
                .append(label)
                .append(" is ")
                .append(result);

        return new Response(true, "FilterLessThanLabel successfully completed.", stringBuilder);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(String value1, MusicBand value2, int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(MusicBand value1, int client_id) {
        throw new IllegalArgumentException("Not supported");
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String commandInfo() {
        return "вывести количество элементов, значение поля label которых меньше заданного";
    }
}
