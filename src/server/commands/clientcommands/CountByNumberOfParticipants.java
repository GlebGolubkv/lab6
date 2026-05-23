package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;
import common.dataclasses.Colors;

public class CountByNumberOfParticipants extends Command {
    @Override
    public Response execute(int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(String value1, int client_id) {

        ClassesManager classesManager = ClassesManager.getInstance();
        int number_of_participants = checkInteger(value1);
        long result;
        synchronized (classesManager) {
            result = classesManager.getCollection().keySet().stream()
                    //Превратили в список значений numberOfParticipants
                    .map(e -> classesManager.getCollection().get(e).getNumberOfParticipants())
                    // Отфильтровали совпадающие
                    .filter(e -> e == number_of_participants).count();

        }
        StringBuilder stringBuilder = new StringBuilder()
                .append("The number participants of equal " + Colors.GREEN)
                .append(number_of_participants).append(Colors.RESET).append("s is ")
                .append(Colors.GREEN).append(result).append(Colors.RESET);


        return new Response(true, "CountByNumberOfParticipants successfully completed.", stringBuilder);

    }

    @Override
    public Response execute(String value1, MusicBand value2, int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(MusicBand value1, int client_id) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "вывести количество элементов, значение поля numberOfParticipants которых равно заданному";
    }

    private int checkInteger(String key) {
        int newKey;
        // если не является числом
        try {
            newKey = Integer.parseInt(key);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Must be an integer");
        }
        return newKey;
    }
}
