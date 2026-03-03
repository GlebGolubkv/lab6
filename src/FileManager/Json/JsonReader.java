package FileManager.Json;

import DataClasses.MusicBand;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Класс, который позволяет читать данные из файла
 */
public class JsonReader {

    String fileName;

    public JsonReader(String filename) {
        this.fileName = filename;
    }


    public Hashtable<Integer, MusicBand> readFile() {

        // Если файл не существует или пустой, возвращаем пустую таблицу
        if (!new File(fileName).exists() || (new File(fileName).length() == 0)) {
            return new Hashtable<>();
        }


        try (FileInputStream file = new FileInputStream(fileName)) {

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


            Hashtable<Integer, MusicBand> map = mapper.readValue(file, new TypeReference<Hashtable<Integer, MusicBand>>() {
            });

            return map;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
