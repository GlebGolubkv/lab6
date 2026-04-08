package server.data;

import common.dataclasses.MusicBand;
import common.dataclasses.CommandType;
import common.Response;
import server.commands.*;
import server.commands.clientcommands.*;

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
    private static final HashMap<CommandType, Command> commands = new HashMap<>();
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
        commands.put(CommandType.HELP, new Help());
        commands.put(CommandType.INFO, new Info());
        commands.put(CommandType.SHOW, new Show());
        commands.put(CommandType.INSERT, new Insert());
        commands.put(CommandType.UPDATE, new Update());
        commands.put(CommandType.REMOVE_KEY, new RemoveKey());
        commands.put(CommandType.CLEAR, new Clear());
        commands.put(CommandType.EXECUTE_SCRIPT, new ExecuteScript());
        commands.put(CommandType.REMOVE_LOWER, new RemoveLower());
        commands.put(CommandType.REPLACE_IF_GREATER, new ReplaceIfGreater());
        commands.put(CommandType.REPLACE_IF_LOWER, new ReplaceIfLower());
        commands.put(CommandType.COUNT_BY_NUMBER_OF_PARTICIPANTS, new CountByNumberOfParticipants());
        commands.put(CommandType.FILTER_LESS_THEN_LABEL, new FilterLessThanLabel());
        commands.put(CommandType.PRINT_FIELD_DESCENDING_LABEL, new PrintFieldDescendingLabel());
        commands.put(CommandType.BEGIN_TRANSACTION, new BeginTransaction());
        commands.put(CommandType.COMMIT_TRANSACTION, new CommitTransaction());
    }

    /**
     * Returns a list of all registered command names.
     *
     * @return an ArrayList containing the names of all commands
     */
    public ArrayList<CommandType> getCommands() {
        return new ArrayList<>(commands.keySet());
    }

    /**
     * Registers a new command with the specified name.
     *
     * @param commandName the name under which the command will be registered
     * @param command     the command implementation
     */
    public void addCommand(CommandType commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     * Retrieves a command by its name.
     *
     * @param commandName the name of the command
     * @return the Command object, or null if no command with that name exists
     */
    public Command getCommand(CommandType commandName) {
        return commands.get(commandName);
    }
    public String getCommandName(CommandType commandType) {
        return  commandType.getCommandName();
    }


    public Response createCommand(CommandType commandType, String argument, MusicBand musicBand) {


        if (commandType.requiresArgument() && commandType.requiresMusicBand()){

            return commands.get(commandType).execute(argument, musicBand);

        } else if (commandType.requiresArgument()){
            return commands.get(commandType).execute(argument);

        } else if (commandType.requiresMusicBand()){
            return commands.get(commandType).execute(musicBand);

        } else  {
            return commands.get(commandType).execute();
        }
    }

}