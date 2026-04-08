package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;
import common.dataclasses.Colors;

public class FilterLessThanLabel extends Command {
    @Override
    public Response execute() {
        throw new IllegalArgumentException("Not supported");
    }


    @Override
    public Response execute(String value1) {
        int label = checkInteger(value1);
        ClassesManager classesManager = ClassesManager.getInstance();

        long result = classesManager.getMap().keySet().stream()
                //Делаем список из полей Label
                .map(e -> classesManager.getMap().get(e).getLabel().getBands())
                .filter(e -> e < label)
                .count();



        StringBuilder stringBuilder = new StringBuilder().append("The number of elements less than "
                + Colors.GREEN + label + Colors.RESET + " is " + Colors.GREEN + result + Colors.RESET);

        return new Response(true, "FilterLessThanLabel successfully completed.", stringBuilder);

    }

    @Override
    public Response execute(String value1, MusicBand value2) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Response execute(MusicBand value1) {
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

    @Override
    public String commandInfo() {
        return "вывести количество элементов, значение поля label которых меньше заданного";
    }
}
