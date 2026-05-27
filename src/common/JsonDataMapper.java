package common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Синглтон-обёртка над {@link ObjectMapper} для сериализации данных клиента и сервера.
 * Компактный JSON (без отступов), даты в ISO-формате.
 */
public class JsonDataMapper {

    private static JsonDataMapper instance;

    private final ObjectMapper mapper;

    private JsonDataMapper() {
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Возвращает инициализированный маппер.
     *
     * @return экземпляр {@code JsonDataMapper}
     * @throws NullPointerException если {@link #initialize()} ещё не вызывался
     */
    public static JsonDataMapper getInstance() {
        if (instance == null) {
            throw new NullPointerException("Mapper has not been initialized");
        }
        return instance;
    }

    /**
     * Создаёт единственный экземпляр с настроенным {@link ObjectMapper}.
     *
     * @throws IllegalStateException при повторной инициализации
     */
    public static void initialize() {
        if (instance == null) {
            instance = new JsonDataMapper();
        } else {
            throw new IllegalStateException("Mapper has already been initialized");
        }
    }

    /**
     * Возвращает настроенный {@link ObjectMapper} для сериализации и десериализации.
     *
     * @return экземпляр Jackson {@code ObjectMapper}
     */
    public ObjectMapper getMapper() {
        return mapper;
    }
}
