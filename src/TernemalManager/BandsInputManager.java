package TernemalManager;

import Data.IDGenerator;
import DataClasses.Coordinates;
import DataClasses.Label;
import DataClasses.MusicBand;
import DataClasses.MusicGenre;

import java.time.ZonedDateTime;

import java.util.Scanner;

/**
 * Класс, который позволяет вводить новый объект MusicBand
 */

public class BandsInputManager {

    /**
     * Базовый ввод нового объекта
     * @param scanner
     * @return введенный MusicBand
     */

    public MusicBand InputBand(Scanner scanner) {

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
     * Кастомный ввод нового обьекта, где ID задается вручную
     * @param ID
     * @param scanner
     * @return Введенный кастомный MusicBand
     */

    public MusicBand InputBand(int ID, Scanner scanner) {

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


    private String readName(Scanner scanner) {
        do {
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Error: name cannot be empty.");
            } else return name;
        } while (true);
    }

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
