package server.filemanager;

import common.dataclasses.Coordinates;
import common.dataclasses.Label;
import common.dataclasses.MusicBand;
import common.dataclasses.MusicGenre;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * Чтение полей объекта {@link MusicBand} из текстового потока (скрипт или файл).
 */
public class BandsFileReader {

    private static BandsFileReader instance;

    private BandsFileReader() {
    }

    /**
     * Возвращает единственный экземпляр читателя групп.
     *
     * @return инициализированный {@link BandsFileReader}
     * @throws IllegalStateException если читатель не инициализирован
     */
    public static BandsFileReader getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BandsInputFromFile has not been initialized");
        }
        return instance;
    }

    /**
     * Инициализирует читатель групп (выполняется один раз).
     *
     * @throws IllegalStateException при повторной инициализации
     */
    public static void initialize() {
        if (instance == null) {
            instance = new BandsFileReader();
        } else {
            throw new IllegalStateException("BandsInputFromFile has already been initialized");
        }
    }

    /**
     * Считывает музыкальную группу из потока; идентификатор в БД назначается при вставке.
     *
     * @param bufferedReader поток ввода полей группы
     * @return прочитанная {@link MusicBand}
     */
    public MusicBand inputBand(BufferedReader bufferedReader) {

        try {

            Integer Id = 0; 

            String Name = readName(bufferedReader);  

            Coordinates coordinates = new Coordinates(readXCoordinates(bufferedReader), readYCoordinates(bufferedReader)); 

            ZonedDateTime time = ZonedDateTime.now(); 

            long numberOfParticipants = readNumberOfParticipants(bufferedReader); 

            long albumsCount = readAlbumsCount(bufferedReader); 

            MusicGenre musicGenre = readMusicGenre(bufferedReader); 

            Label label = readLabel(bufferedReader); 

            MusicBand musicBand = new MusicBand(Id, Name, coordinates, time, numberOfParticipants, albumsCount, musicGenre, label);

            return musicBand;
        } catch (Exception e) {
            throw bandReadException(e);
        }
    }

    /**
     * Считывает музыкальную группу из потока с заданным идентификатором (для update).
     *
     * @param Id             идентификатор объекта
     * @param bufferedReader поток ввода полей группы
     * @return прочитанная {@link MusicBand}
     */
    public MusicBand inputBand(Integer Id, BufferedReader bufferedReader) {

        try {

            String Name = readName(bufferedReader);  

            Coordinates coordinates = new Coordinates(readXCoordinates(bufferedReader), readYCoordinates(bufferedReader)); 

            ZonedDateTime time = ZonedDateTime.now(); 

            long numberOfParticipants = readNumberOfParticipants(bufferedReader); 

            long albumsCount = readAlbumsCount(bufferedReader); 

            MusicGenre musicGenre = readMusicGenre(bufferedReader); 

            Label label = readLabel(bufferedReader); 

            MusicBand musicBand = new MusicBand(Id, Name, coordinates, time, numberOfParticipants, albumsCount, musicGenre, label);

            return musicBand;
        } catch (Exception e) {
            throw bandReadException(e);
        }
    }

    private static IllegalArgumentException bandReadException(Exception e) {
        String detail = e.getMessage();
        if (detail == null || detail.isBlank()) {
            detail = e.getClass().getSimpleName();
        }
        return new IllegalArgumentException("Error reading MusicBand from script: " + detail, e);
    }

    private String readDataLine(BufferedReader bufferedReader) throws IOException {
        String line;
        do {
            line = bufferedReader.readLine();
            if (line == null) {
                throw new IllegalStateException("Unexpected end of script file while reading MusicBand fields");
            }
            line = line.trim();
            if (!line.isEmpty() && line.charAt(0) == '\ufeff') {
                line = line.substring(1).trim();
            }
        } while (line.isEmpty() || line.startsWith("#"));
        return line;
    }

    private String readName(BufferedReader bufferedReader) throws IOException {
        String name = readDataLine(bufferedReader);
        if (name.isEmpty()) {
            throw new IllegalStateException("Error: name cannot be empty.");
        } else return name;
    }

    private Integer readXCoordinates(BufferedReader bufferedReader) throws IOException {

        String x = readDataLine(bufferedReader);

        if (x.isEmpty()) {

            throw new IllegalStateException("Error: coordinate cannot be empty.");
        } else try {
            int x1 = Integer.parseInt(x);
            if (x1 <= 254) {
                return x1;
            } else {

                throw new IllegalStateException("Error: coordinate must be less then 254.");
            }
        } catch (NumberFormatException e) {

            throw new IllegalStateException("Error: coordinate must be an integer.");
        }

    }

    private Double readYCoordinates(BufferedReader bufferedReader) throws IOException {

        String y = readDataLine(bufferedReader);

        if (y.isEmpty()) {

            throw new IllegalStateException("Error: coordinate cannot be empty.");
        } else {
            try {
                Double y1 = Double.parseDouble(y);
                if (y1 <= 93) {
                    return y1;
                } else {

                    throw new IllegalArgumentException("Error: coordinate must be less then 93.");
                }
            } catch (NumberFormatException e) {

                throw new IllegalArgumentException("Error: coordinate must be an integer.");
            }
        }

    }

    private long readNumberOfParticipants(BufferedReader bufferedReader) throws IOException {

        String n = readDataLine(bufferedReader);

        if (n.isEmpty()) {

            throw new IllegalArgumentException("Error: Number cannot be empty.");
        } else try {
            long n1 = Long.parseLong(n);
            if (n1 > 0) {
                return n1;
            } else {
                throw new IllegalArgumentException("Error: Number must be greater than 0.");
            }
        } catch (NumberFormatException e) {

            throw new IllegalArgumentException("Error: Number must be double.");
        }
    }

    private long readAlbumsCount(BufferedReader bufferedReader) throws IOException {

        String n = readDataLine(bufferedReader);

        if (n.isEmpty()) {

            throw new IllegalArgumentException("Error: Number cannot be empty.");
        } else try {
            long n1 = Long.parseLong(n);
            if (n1 > 0) {
                return n1;
            } else {
                throw new IllegalArgumentException("Error: Number must be greater than 0.");
            }
        } catch (NumberFormatException e) {

            throw new IllegalArgumentException("Error: Number must be double.");
        }

    }

    private MusicGenre readMusicGenre(BufferedReader bufferedReader) throws IOException {

        String genre = readDataLine(bufferedReader);
        if (genre.isEmpty()) {
            return null;
        } else {
            try {
                return MusicGenre.valueOf(genre.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Error: Invalid genre.");
            }
        }

    }

    private Label readLabel(BufferedReader bufferedReader) {

        try {
            Integer x = Integer.parseInt(readDataLine(bufferedReader));
            return new Label(x);
        } catch (IllegalArgumentException | IOException e) {
            throw new IllegalArgumentException("Error: Invalid label.");
        }
    }

}
