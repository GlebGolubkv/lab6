package commandsmanager;

import dataclasses.MusicBand;

/**
 * Abstract base class for all commands in the application.
 * Defines the contract for executing commands with different parameter sets
 * and providing command information.
 */
public abstract class Command {

    public Command() {}

    /**
     * Executes the command without any parameters.
     */
    public abstract void execute();

    /**
     * Executes the command with a single string parameter.
     *
     * @param value1 the string argument for the command
     */
    public abstract void execute(String value1);

    /**
     * Executes the command with a string parameter and a MusicBand object.
     *
     * @param value1 the string argument for the command
     * @param value2 the MusicBand object to be processed
     */
    public abstract void execute(String value1, MusicBand value2);

    /**
     * Executes the command with a MusicBand object.
     *
     * @param value1 the MusicBand object to be processed
     */
    public abstract void execute(MusicBand value1);

    /**
     * Provides information about the command.
     *
     * @return a string describing the command's purpose and usage
     */
    public abstract String commandInfo();
}