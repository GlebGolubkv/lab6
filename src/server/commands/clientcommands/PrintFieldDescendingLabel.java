package server.commands.clientcommands;

import server.commands.Command;
import server.data.ClassesManager;
import common.dataclasses.MusicBand;
import common.Response;

/**
 * Команда вывода значений поля label всех элементов в порядке убывания.
 */
public class PrintFieldDescendingLabel extends Command {

    /**
     * {@inheritDoc}
     */
    @Override
    public Response execute(int client_id) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Labels: ");

        synchronized (ClassesManager.getInstance().getCollection()) {

            ClassesManager.getInstance().getCollection().keySet().stream()
                    .map(e -> ClassesManager.getInstance().getCollection().get(e).getLabel().getBands())
                    .sorted((a, b) -> b - a)
                    .forEach(e -> stringBuilder.append(e).append(" "));
        }

        return new Response(true, "PrintFieldDescendingLabel successfully completed.", stringBuilder);

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
        return "вывести значения поля label всех элементов в порядке убывания";
    }
}
