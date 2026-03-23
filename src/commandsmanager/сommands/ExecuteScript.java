package commandsmanager.сommands;

import commandsmanager.Command;
import dataclasses.MusicBand;
import filemanager.commandsparser.CommandsReader;
import termenalmanager.Colors;

public class ExecuteScript extends Command {
    @Override
    public void execute() {
        throw new IllegalArgumentException("Not supported");

    }

    @Override
    public void execute(String value1) {
        System.out.println();
        System.out.println("Read commands from file");
        System.out.println();

        CommandsReader.getInstance().readCommands(value1);
        CommandsReader.getInstance().resetCommand();
        // после обнаружения рекурсии - рекурсивно отчищает несколько
        // раз список с командами, так как в каждом запросе идет посл выполнения внутренней рекурсии

    }

    @Override
    public void execute(String value1, MusicBand value2) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void execute(MusicBand value1) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "считать и исполнить скрипт из указанного файла";
    }
}
