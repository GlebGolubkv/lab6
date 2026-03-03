package CommandsManager.Commands;

import CommandsManager.Command;
import Data.ClassesManager;
import TernemalManager.Colors;

public class Save extends Command {
    @Override
    public void execute() {
        ClassesManager classesManager = new ClassesManager();
        classesManager.SaveCollectionToFile();

        System.out.println();
        System.out.println(Colors.GREEN + "The collection has been saved" + Colors.RESET);
        System.out.println();

    }

    @Override
    public void execute(String value1) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "сохранить коллекцию в файл";
    }
}
