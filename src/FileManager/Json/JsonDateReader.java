package FileManager.Json;

import TernemalManager.Colors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Класс считывает дату создания коллекции
 */
public class JsonDateReader {

    String fileName;
    public JsonDateReader(String fileName) {
        this.fileName = fileName;

    }

    /**
     * Метод, который считывает дату создания коллекции
     * @return Дата создания коллекции
     */
    public String getCreationTime() {
        Path path = Paths.get(fileName);

        try {
            FileTime creationTime = (FileTime) Files.getAttribute(path, "creationTime");
            ZonedDateTime created = ZonedDateTime.ofInstant(
                    creationTime.toInstant(), ZoneId.systemDefault());
            return (Colors.GREEN +  "Creation date: " + Colors.RESET + created);
        } catch (UnsupportedOperationException e) {
            return ("it is impossible to make an operation");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }




