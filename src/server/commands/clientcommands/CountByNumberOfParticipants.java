package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;

/**
 * Команда подсчёта элементов с заданным числом участников.
 */
public class CountByNumberOfParticipants extends Command {

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

        ClassesManager classesManager = ClassesManager.getInstance();
        int number_of_participants = checkInteger(value1);
        long result;
        synchronized (classesManager) {
            result = classesManager.getCollection().keySet().stream()
                    
                    .map(e -> classesManager.getCollection().get(e).getNumberOfParticipants())
                    
                    .filter(e -> e == number_of_participants).count();

        }
        StringBuilder stringBuilder = new StringBuilder()
                .append("The number participants of equal ")
                .append(number_of_participants)
                .append("s is ")
                .append(result);

        return new Response(true, "CountByNumberOfParticipants successfully completed.", stringBuilder);

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

    /**
     * {@inheritDoc}
     */
    @Override
    public String commandInfo() {
        return "вывести количество элементов, значение поля numberOfParticipants которых равно заданному";
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
