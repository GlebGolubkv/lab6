package FileManager.Json;

import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Класс, который чистит файл
 * <p>
 *     Класс чистит файл, не редактируя при этом ранее считанную коллекцию
 * </p>
 */
public class JsonCleaner {

    String fileName;

    public JsonCleaner(String fileName) {
        this.fileName = fileName;
    }

    public void cleanFile() {
        try (FileOutputStream file = new FileOutputStream(fileName, true)) {
            //Ничего не вызываем. Перезаписываем файл в нулевой
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
