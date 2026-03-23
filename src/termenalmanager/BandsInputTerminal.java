package termenalmanager;

import data.IDGenerator;
import dataclasses.Coordinates;
import dataclasses.Label;
import dataclasses.MusicBand;
import dataclasses.MusicGenre;

import java.time.ZonedDateTime;
import java.util.Scanner;

/**
 * Provides interactive terminal input for creating MusicBand objects.
 * Handles prompting the user for each field, validating input, and constructing the corresponding MusicBand instance.
 * Implements the Singleton pattern to ensure a single point of terminal-based band input.
 */
public class BandsInputTerminal {

    private static BandsInputTerminal instance;

    private BandsInputTerminal() {
    }

    /**
     * Returns the singleton instance of BandsInputTerminal.
     *
     * @return the singleton instance
     * @throws IllegalStateException if the instance has not been initialized
     */
    public static BandsInputTerminal getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BandsInputManager has not been initialized");
        }
        return instance;
    }

    /**
     * Initializes the singleton instance of BandsInputTerminal.
     * Must be called once before using the instance.
     *
     * @throws IllegalStateException if the instance has already been initialized
     */
    public static void initialize() {
        if (instance == null) {
            instance = new BandsInputTerminal();
        } else {
            throw new IllegalStateException("BandsInputManager has already been initialized");
        }
    }

    /**
     * Prompts the user via the provided Scanner to enter all fields of a MusicBand.
     * Generates a new ID automatically.
     *
     * @param scanner the Scanner to read user input from
     * @return a newly created MusicBand object with the entered data
     */
    public MusicBand inputBand(Scanner scanner) {

        System.out.println();
        System.out.println(Colors.GREEN + "Enter music band details:" + Colors.RESET);

        Integer Id = new IDGenerator().generateNewID(); // ID

        String Name = readName(scanner);  //NAME

        Coordinates coordinates = new Coordinates(readXCoordinates(scanner), readYCoordinates(scanner)); // COORDINATES

        ZonedDateTime time = ZonedDateTime.now(); // TIME

        long numberOfParticipants = readNumberOfParticipants(scanner); // participants

        long albumsCount = readAlbumsCount(scanner); // albums

        MusicGenre musicGenre = readMusicGenre(scanner); // genre

        Label label = readLabel(scanner); // label

        MusicBand musicBand = new MusicBand(Id, Name, coordinates, time, numberOfParticipants, albumsCount, musicGenre, label);

        System.out.println("Вы ввели: " + musicBand);
        return musicBand;
    }

    /**
     * Prompts the user via the provided Scanner to enter all fields of a MusicBand,
     * using a manually specified ID instead of generating a new one.
     *
     * @param ID      the ID to assign to the MusicBand
     * @param scanner the Scanner to read user input from
     * @return a newly created MusicBand object with the entered data and the given ID
     */
    public MusicBand inputBand(int ID, Scanner scanner) {

        System.out.println("Enter music band details:");

        Integer Id = ID; // ID

        String Name = readName(scanner);    //NAME

        Coordinates coordinates = new Coordinates(readXCoordinates(scanner), readYCoordinates(scanner)); // COORDINATES

        ZonedDateTime time = ZonedDateTime.now(); // TIME

        long numberOfParticipants = readNumberOfParticipants(scanner); // participants

        long albumsCount = readAlbumsCount(scanner); // albums

        MusicGenre musicGenre = readMusicGenre(scanner); // genre

        Label label = readLabel(scanner); // label

        MusicBand musicBand = new MusicBand(Id, Name, coordinates, time, numberOfParticipants, albumsCount, musicGenre, label);

        System.out.println("Вы ввели: " + musicBand);
        return musicBand;
    }

    /**
     * Reads and validates the band name from the scanner.
     * Continues prompting until a non-empty name is entered.
     *
     * @param scanner the Scanner to read from
     * @return the validated name
     */
    private String readName(Scanner scanner) {
        do {
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Error: name cannot be empty.");
            } else return name;
        } while (true);
    }

    /**
     * Reads and validates the x-coordinate from the scanner.
     * Continues prompting until a valid integer ≤ 254 is entered.
     *
     * @param scanner the Scanner to read from
     * @return the validated x-coordinate
     */
    private Integer readXCoordinates(Scanner scanner) {

        while (true) {

            System.out.print("Enter X coordinate: ");
            String x = scanner.nextLine().trim();

            if (x.isEmpty()) {

                System.out.println("Error: coordinate cannot be empty.");
            } else try {
                int x1 = Integer.parseInt(x);
                if (x1 <= 254) {
                    return x1;
                } else {

                    System.out.println("Error: coordinate must be less then 254.");
                }
            } catch (NumberFormatException e) {

                System.out.println("Error: coordinate must be an integer.");
            }
        }

    }

    /**
     * Reads and validates the y-coordinate from the scanner.
     * Continues prompting until a valid double ≤ 93 is entered.
     *
     * @param scanner the Scanner to read from
     * @return the validated y-coordinate
     */
    private Double readYCoordinates(Scanner scanner) {

        while (true) {

            System.out.print("Enter Y coordinate: ");
            String y = scanner.nextLine().trim();

            if (y.isEmpty()) {

                System.out.println("Error: coordinate cannot be empty.");
            } else {
                try {
                    Double y1 = Double.parseDouble(y);
                    if (y1 <= 93) {
                        return y1;
                    } else {

                        System.out.println("Error: coordinate must be less then 93.");
                    }
                } catch (NumberFormatException e) {

                    System.out.println("Error: coordinate must be an integer.");
                }
            }
        }
    }

    /**
     * Reads and validates the number of participants from the scanner.
     * Continues prompting until a valid long > 0 is entered.
     *
     * @param scanner the Scanner to read from
     * @return the validated number of participants
     */
    private long readNumberOfParticipants(Scanner scanner) {
        while (true) {

            System.out.print("Enter number of participants: ");
            String n = scanner.nextLine().trim();

            if (n.isEmpty()) {

                System.out.println("Error: Number cannot be empty.");
            } else try {
                long n1 = Long.parseLong(n);
                if (n1 > 0) {
                    return n1;
                } else {
                    System.out.println("Error: Number must be greater than 0.");
                }
            } catch (NumberFormatException e) {

                System.out.println("Error: Number must be double.");
            }
        }
    }

    /**
     * Reads and validates the albums count from the scanner.
     * Continues prompting until a valid long > 0 is entered.
     *
     * @param scanner the Scanner to read from
     * @return the validated albums count
     */
    private long readAlbumsCount(Scanner scanner) {
        while (true) {

            System.out.print("Enter number of albums: ");
            String n = scanner.nextLine().trim();

            if (n.isEmpty()) {

                System.out.println("Error: Number cannot be empty.");
            } else try {
                long n1 = Long.parseLong(n);
                if (n1 > 0) {
                    return n1;
                } else {
                    System.out.println("Error: Number must be greater than 0.");
                }
            } catch (NumberFormatException e) {

                System.out.println("Error: Number must be double.");
            }
        }
    }

    /**
     * Reads and validates the music genre from the scanner.
     * Continues prompting until a valid genre or empty input (for null) is entered.
     *
     * @param scanner the Scanner to read from
     * @return the corresponding MusicGenre enum value, or null if input is empty
     */
    private MusicGenre readMusicGenre(Scanner scanner) {
        while (true) {
            System.out.println("Available genres: SOUL, BLUES, MATH_ROCK, PUNK_ROCK, BRIT_POP or null");
            String genre = scanner.nextLine().trim();
            if (genre.isEmpty()) {
                return null;
            } else {
                try {
                    return MusicGenre.valueOf(genre.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: Invalid genre.");
                }
            }

        }
    }

    /**
     * Reads and validates the label bands count from the scanner and constructs a Label object.
     * Continues prompting until a valid integer is entered.
     *
     * @param scanner the Scanner to read from
     * @return a Label object with the parsed bands count
     */
    private Label readLabel(Scanner scanner) {
        while (true) {
            System.out.print("Enter Label: ");
            try {
                Integer x = Integer.parseInt(scanner.nextLine().trim());
                return new Label(x);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: Invalid label.");
            }
        }
    }
}