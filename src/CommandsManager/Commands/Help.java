package CommandsManager.Commands;

import CommandsManager.Command;
import Data.DataCommands;
import TernemalManager.Colors;

import java.awt.*;

public class Help extends Command {
    public Help() {}


    @Override
    public void execute() {
        StringBuilder helpCommands = new StringBuilder();
        DataCommands data = new DataCommands();
        for (String name : data.getNames()){
            helpCommands.append(Colors.WHITE + "Command: " + Colors.GREEN)
                    .append(name).append(" : " + Colors.RESET).append(data.getCommand(name).getCommandIfo()).append("\n");

        }
        System.out.println();
        System.out.println( Colors.GREEN +"Commands: " + Colors.RESET);
        System.out.println(helpCommands);
        System.out.println();

    }

    @Override
    public void execute(String value1) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "вывести справку по доступным командам";
    }

}
