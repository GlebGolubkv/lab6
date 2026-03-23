package filemanager.commandsparser;

import data.ClassesManager;
import data.DataCommands;
import dataclasses.MusicBand;
import termenalmanager.BandsFileReader;
import termenalmanager.Colors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Handles reading and execution of commands from script files.
 * Commands are read line by line, each line containing a command and optional arguments.
 * Supports recursion detection to prevent infinite loops.
 */
public class CommandsReader {

    private static CommandsReader instance;
    /**
     * List of file names that have been executed in the current script chain.
     * Used for recursion detection.
     */
    private static final ArrayList<String> filesNames = new ArrayList<>();

    /**
     * Private constructor for singleton pattern.
     */
    private CommandsReader() {

    }

    /**
     * Initializes the singleton instance of CommandsReader.
     * Must be called once before using the instance.
     *
     * @throws IllegalStateException if the instance has already been initialized
     */
    public static void initialize() {

        if (instance == null) {
            instance = new CommandsReader();
        } else {
            throw new IllegalStateException("CommandsReader has already been initialized");
        }
    }

    /**
     * Returns the singleton instance of CommandsReader.
     *
     * @return the singleton instance
     * @throws IllegalStateException if the instance has not been initialized
     */
    public static CommandsReader getInstance() {
        if (instance == null) {
            throw new IllegalStateException("CommandsReader has not been initialized");
        }
        return instance;
    }

    /**
     * Reads and executes commands from the specified file.
     * Processes each line, checks for recursion, and executes the appropriate command.
     * If a command requires a MusicBand object, it is read from the file using BandsFileReader.
     *
     * @param fileName the name of the file to read commands from
     */
    public void readCommands(String fileName) {

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName.trim()));

            String line;

            while ((line = bufferedReader.readLine()) != null) {

                if (!checkRecursion(line)) {

                    try {
                        // Возвращает null, если в строке команда, которой не нужен musicBand. Возвращает musicBand, если его ввод прошел без ошибок.
                        // Выкидывает ошибку, если что-то пошло не так
                        MusicBand musicBand = checkIfBandsInput(bufferedReader, line);

                        if (musicBand != null) {
                            DataCommands.getInstance().createCommandByName(line, musicBand);
                        } else {
                            DataCommands.getInstance().createCommandByName(line);
                        }
                        System.out.println("Command " + line + " processed");

                    } catch (Exception e) {
                        if (ClassesManager.getInstance().isInTransaction()) {
                            ClassesManager.getInstance().rollbackTransaction();
                        }
                        System.out.println((Colors.RED + "Error reading MusicBand from file.") + Colors.RESET);


                    }

                }

            }

        } catch (IOException e) {
            throw new RuntimeException("There is no file with this name.");

        }
    }

    /**
     * Checks if executing the given line would cause a recursion.
     * Recursion occurs when an 'execute_script' command attempts to run a file that is already in the call stack.
     *
     * @param line the command line to check
     * @return true if recursion is detected, false otherwise
     */
    private boolean checkRecursion(String line) {

        String[] command = line.toLowerCase().split("\\s+");

        if (Objects.equals(command[0], "execute_script")) {
            if (filesNames.contains(command[1])) {
                System.out.println(Colors.RED + "A recursion was detected. Executing the file " + Colors.GREEN + command[1] + Colors.RED + " will cause the program to loop. " + Colors.RESET);
                return true;
            } else {
                filesNames.add(command[1]);
            }
        }
        return false;
    }

    /**
     * Clears the list of executed file names, resetting recursion detection.
     * Should be called after script execution is complete to allow new script chains.
     */
    public void resetCommand() {
        filesNames.clear();
    }

    /**
     * Determines if the given command requires a MusicBand object and reads it from the BufferedReader.
     * Supported commands that require a MusicBand: remove_lower, update, insert, replace_if_greater, replace_if_lower.
     *
     * @param reader the BufferedReader to read MusicBand data from
     * @param line   the command line
     * @return a MusicBand object if the command requires it and reading succeeds, otherwise null
     * @throws IllegalArgumentException if an error occurs while reading the MusicBand data
     */
    private MusicBand checkIfBandsInput(BufferedReader reader, String line) {
        String[] command = line.toLowerCase().split("\\s+");

        if (Objects.equals(command[0], "remove_lower") & command.length == 1) {
            try {
                MusicBand musicBand = BandsFileReader.getInstance().inputBand(reader);
                return musicBand;
            } catch (Exception e) {
                throw new IllegalArgumentException("Error reading MusicBand from file.");

            }

        } else if (Objects.equals(command[0], "update") & command.length == 2) {
            try {
                MusicBand musicBand = BandsFileReader.getInstance().inputBand(Integer.parseInt(command[1]), reader);
                return musicBand;
            } catch (Exception e) {
                throw new IllegalArgumentException("Error reading MusicBand from file.");

            }

        } else if ((Objects.equals(command[0], "insert") || Objects.equals(command[0], "replace_if_greater") || Objects.equals(command[0], "replace_if_lower")) & command.length == 2) {

            try {
                MusicBand musicBand = BandsFileReader.getInstance().inputBand(reader);
                return musicBand;
            } catch (Exception e) {
                throw new IllegalArgumentException("Error reading MusicBand from file.");

            }
        } else return null;
    }


}