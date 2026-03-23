package filemanager.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Provides a configured Jackson ObjectMapper for JSON processing.
 * Configures the mapper with JavaTimeModule for Java 8 date/time support,
 * enables pretty-printing, and disables writing dates as timestamps.
 * Implements the Singleton pattern to provide a single, consistently configured mapper.
 */
public class JsonDataMapper {

    private static JsonDataMapper instance;
    /**
     * The configured ObjectMapper instance.
     */
    private final ObjectMapper mapper;

    /**
     * Private constructor that initializes and configures the ObjectMapper.
     * Registers the JavaTimeModule, enables pretty-printing, and disables timestamp writing.
     */
    private JsonDataMapper() {

        this.mapper = new ObjectMapper();
        //регаем новый модуль
        mapper.registerModule(new JavaTimeModule());
        //выставляем читаемую запись в файле
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        //выставляем отображение времени в файле
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    /**
     * Returns the singleton instance of JsonDataMapper.
     *
     * @return the singleton instance
     * @throws NullPointerException if the instance has not been initialized
     */
    public static JsonDataMapper getInstance() {
        if (instance == null) {
            throw new NullPointerException("Mapper has not been initialized");
        }
        return instance;
    }

    /**
     * Initializes the singleton instance of JsonDataMapper.
     * Must be called once before using the instance.
     *
     * @throws IllegalStateException if the instance has already been initialized
     */
    public static void initialize() {
        if (instance == null) {
            instance = new JsonDataMapper();
        } else {
            throw new IllegalStateException("Mapper has already been initialized");
        }
    }

    /**
     * Returns the configured ObjectMapper.
     *
     * @return the ObjectMapper instance
     */
    public ObjectMapper getMapper() {
        return mapper;
    }
}