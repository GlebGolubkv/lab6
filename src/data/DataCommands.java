package data;

import commandsmanager.Command;
import commandsmanager.сommands.*;
import dataclasses.MusicBand;
import termenalmanager.Colors;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages the registration and execution of commands.
 * Implements the Singleton pattern to provide a single point of access to all available commands.
 * Commands are stored in a HashMap with their names as keys.
 */
public class DataCommands {

    /**
     * A map that stores all available commands, keyed by their names.
     */
    private static final HashMap<String, Command> commands = new HashMap<>();
    private static DataCommands instance;

    /**
     * Returns the singleton instance of DataCommands.
     *
     * @return the singleton instance
     * @throws IllegalStateException if the instance has not been initialized
     */
    public static DataCommands getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DataCommands has not been initialized");
        }
        return instance;
    }

    /**
     * Initializes the singleton instance of DataCommands.
     * This method must be called once before using the instance.
     *
     * @throws IllegalStateException if the instance has already been initialized
     */
    public static void initialize() {
        if (instance == null) {
            instance = new DataCommands();
        } else {
            throw new IllegalStateException("DataCommands has already been initialized");
        }
    }

    /**
     * Private constructor that populates the command map with all predefined commands.
     */
    private DataCommands() {
        commands.put("help", new Help());
        commands.put("info", new Info());
        commands.put("show", new Show());
        commands.put("insert", new Insert());
        commands.put("update", new Update());
        commands.put("remove_key", new RemoveKey());
        commands.put("clear", new Clear());
        commands.put("save", new Save());
        commands.put("execute_script", new ExecuteScript());
        commands.put("exit", new Exit());
        commands.put("remove_lower", new RemoveLower());
        commands.put("replace_if_greater", new ReplaceIfGreater());
        commands.put("replace_if_lower", new ReplaceIfLower());
        commands.put("count_by_number_of_participants", new CountByNumberOfParticipants());
        commands.put("filter_less_then_label", new FilterLessThanLabel());
        commands.put("print_field_descending_label", new PrintFieldDescendingLabel());
        commands.put("begin_transaction", new BeginTransaction());
        commands.put("commit_transaction", new CommitTransaction());

    }

    /**
     * Returns a list of all registered command names.
     *
     * @return an ArrayList containing the names of all commands
     */
    public ArrayList<String> getNames() {
        return new ArrayList<>(commands.keySet());
    }

    /**
     * Registers a new command with the specified name.
     *
     * @param commandName the name under which the command will be registered
     * @param command     the command implementation
     */
    public void addCommand(String commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     * Retrieves a command by its name.
     *
     * @param commandName the name of the command
     * @return the Command object, or null if no command with that name exists
     */
    public Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    /**
     * Parses and executes a command based on the input string without additional parameters.
     * The input string may contain the command name only or with one argument.
     *
     * @param commandName the input string containing the command and possibly an argument
     */
    public void createCommandByName(String commandName) {
        try {
            commandName = commandName.trim().toLowerCase();
            if (commandName.split("\\s+").length == 1) {
                if (commands.containsKey(commandName)) {
                    commands.get(commandName).execute();
                } else {
                    throw new IllegalArgumentException("Cannot create command with name " + commandName);
                }
            } else if (commandName.split("\\s+").length == 2) {
                String[] values = commandName.split("\\s+");
                if (commands.containsKey(values[0])) {
                    commands.get(values[0]).execute(values[1]);
                } else {

                    throw new IllegalArgumentException("Cannot create command with name " + values[0]);
                }

            } else {

                throw new IllegalArgumentException("There are too many arguments for the function");

            }
        } catch (Exception e) {
            System.out.println(Colors.RED + e.getMessage() + Colors.RESET);

            if (ClassesManager.getInstance().isInTransaction()) {
                ClassesManager.getInstance().rollbackTransaction();

            }


        }
    }

    /**
     * Parses and executes a command with an additional MusicBand parameter.
     * The input string may contain the command name only or with one argument.
     *
     * @param commandName the input string containing the command and possibly an argument
     * @param musicBand   the MusicBand object to pass to the command
     */
    public void createCommandByName(String commandName, MusicBand musicBand) {
        try {
            commandName = commandName.trim().toLowerCase();
            if (commandName.split("\\s+").length == 1) {
                if (commands.containsKey(commandName)) {
                    commands.get(commandName).execute(musicBand);
                } else {
                    throw new IllegalArgumentException("Cannot create command with name " + commandName);

                }
            } else if (commandName.split("\\s+").length == 2) {
                String[] values = commandName.split("\\s+");
                if (commands.containsKey(values[0])) {
                    commands.get(values[0]).execute(values[1], musicBand);
                } else {
                    throw new IllegalArgumentException("Cannot create command with name " + values[0]);
                }

            } else {

                throw new IllegalArgumentException("There are too many arguments for the function");

            }


        } catch (Exception e) {
            System.out.println(Colors.RED + e.getMessage() + Colors.RESET);

            if (ClassesManager.getInstance().isInTransaction()) {
                ClassesManager.getInstance().rollbackTransaction();

            }
        }
    }
}