package Data;

import CommandsManager.Builder.*;
import CommandsManager.CommandBuilder;
import TernemalManager.Colors;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс, который запускает команды
 * <p>
 *     Получает на вход команды, далее обращаясь к билдерам, которые создают команды
 * </p>
 */
public class DataCommands {

    // список всех команд, поиск по команде
    private static final HashMap<String, CommandBuilder> commands = new HashMap<>();


    public DataCommands() {
        commands.put("help", new HelpBuilder());
        commands.put("info", new InfoBuilder());
        commands.put("show", new ShowBuilder());
        commands.put("insert", new InsertBuilder());
        commands.put("update", new UpdateBuilder());
        commands.put("remove_key", new Remove_keyBuilder());
        commands.put("clear", new ClearBuilder());
        commands.put("save", new SaveBuilder());
        commands.put("execute_script", new Execute_scriptBuilder());
        commands.put("exit", new ExitBuilder());
        commands.put("remove_lower", new Remove_lowerBuilder());
        commands.put("replace_if_greater", new Replace_if_greaterBuilder());
        commands.put("replace_if_lower", new Replace_if_lowerBuilder());
        commands.put("count_by_number_of_participants", new Count_by_number_of_participantsBuilder());
        commands.put("filter_less_then_label", new Filter_less_then_labelBuilder());
        commands.put("print_field_descending_label", new Print_field_descending_labelBuilder());

    }

    /**
     * Метод поучения названий всех команд
     * @return названия всех команд
     */
    public ArrayList<String> getNames() {
        return new ArrayList<>(commands.keySet());
    }


    public void addCommand(String commandName, CommandBuilder commandBuilder) {
        commands.put(commandName, commandBuilder);
    }


    public CommandBuilder getCommand(String commandName) {
        return commands.get(commandName);
    }

    /**
     * Метод, который запускает команду.
     * <p>
     *     Он проверяет сколько переменных было введено, далее создает подходящую команду и передает в нее переменные
     * </p>
     * @param commandName
     */
    public void createCommandByName(String commandName) {
        commandName = commandName.trim().toLowerCase();
        if (commandName.split("\\s+").length == 1) {
            if (commands.containsKey(commandName)) {
                commands.get(commandName).createCommand();
            } else {
                System.out.println();
                System.out.print(Colors.RED + "Cannot create command with name " + commandName + Colors.RESET);
                System.out.println();

            }
        } else if (commandName.split("\\s+").length == 2) {
            String[] values = commandName.split("\\s+");
            if (commands.containsKey(values[0])) {
                commands.get(values[0]).createCommand(values[1]);
            } else {
                System.out.println();
                System.out.print(Colors.RED + "Cannot create command with name " + values[0] + Colors.RESET);
                System.out.println();
            }

        } else {
            System.out.println();
            System.out.println(Colors.RED + "There are too many arguments for the function" + Colors.RESET);
            System.out.println();

        }


    }


}
