package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;
import common.dataclasses.Colors;

public class CountByNumberOfParticipants extends Command {
    @Override
    public Response execute() {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(String value1) {
        ClassesManager classesManager = ClassesManager.getInstance();
        int number_of_participants = checkInteger(value1);


        long result = classesManager.getMap().keySet().stream()
                //Превратили в список значений numberOfParticipants
                .map(e -> classesManager.getMap().get(e).getNumberOfParticipants())
                // Отфильтровали совпадающие
                .filter(e -> e == number_of_participants).count();


        StringBuilder stringBuilder = new StringBuilder().append("The number participants of equal "
                + Colors.GREEN + number_of_participants + Colors.RESET +
                "s is " + Colors.GREEN + result + Colors.RESET);


        return new Response(true, "CountByNumberOfParticipants successfully completed.", stringBuilder);

    }

    @Override
    public Response execute(String value1, MusicBand value2) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(MusicBand value1) {
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
