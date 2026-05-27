package common;

import com.fasterxml.jackson.core.JsonProcessingException;
import common.dataclasses.MusicBandEntry;

import java.util.List;

/**
 * Ответ сервера клиенту: признак успеха, сообщение, дополнительные данные или записи коллекции.
 * Поддерживает служебные поля для внутренних команд и передачи коллекции в GUI.
 */
public class Response {

    private boolean success;
    private String message;
    private StringBuilder data;
    private boolean internalOnly;

    /**
     * @return служебные числовые данные сервера (например, идентификатор сессии)
     */
    public int getServerData() {
        return serverData;
    }

    private int serverData;

    private List<MusicBandEntry> entries;

    /**
     * Конструктор по умолчанию для десериализации JSON.
     */
    public Response() {
    }

    /**
     * Создаёт ответ с признаком успеха и сообщением.
     *
     * @param success признак успешного выполнения
     * @param message текстовое сообщение
     */
    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;

    }

    /**
     * Создаёт ответ для внутренних команд, не предназначенных для вывода клиенту.
     *
     * @param success      признак успешного выполнения
     * @param message      текстовое сообщение
     * @param internalOnly {@code true}, если ответ только для внутренней обработки
     */
    public Response(boolean success, String message, boolean internalOnly) {
        this.success = success;
        this.message = message;
        this.internalOnly = internalOnly;
    }

    /**
     * Создаёт внутренний ответ с дополнительными числовыми данными сервера.
     *
     * @param success      признак успешного выполнения
     * @param message      текстовое сообщение
     * @param internalOnly {@code true}, если ответ только для внутренней обработки
     * @param serverData   служебные данные сервера
     */
    public Response(boolean success, String message, boolean internalOnly, int serverData) {
        this.success = success;
        this.message = message;
        this.internalOnly = internalOnly;
        this.serverData = serverData;
    }

    /**
     * Создаёт ответ с дополнительным текстовым буфером данных.
     *
     * @param success признак успешного выполнения
     * @param message текстовое сообщение
     * @param data    дополнительные данные команды
     */
    public Response(boolean success, String message, StringBuilder data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * Создаёт ответ с записями коллекции (для GUI и команды получения коллекции).
     *
     * @param success признак успешного выполнения
     * @param message текстовое сообщение
     * @param entries список элементов коллекции
     */
    public Response(boolean success, String message, List<MusicBandEntry> entries) {
        this.success = success;
        this.message = message;
        this.entries = entries;
    }

    /**
     * Преобразует ответ в JSON-строку.
     *
     * @return JSON-представление объекта
     * @throws JsonProcessingException при ошибке сериализации
     */
    public String toJson() throws JsonProcessingException {
        return JsonDataMapper.getInstance().getMapper().writeValueAsString(this);
    }

    /**
     * Восстанавливает ответ из JSON-строки.
     *
     * @param json JSON-представление ответа
     * @return десериализованный {@code Response}
     * @throws JsonProcessingException при ошибке разбора
     */
    public static Response fromJson(String json) throws JsonProcessingException {
        return JsonDataMapper.getInstance().getMapper().readValue(json, Response.class);
    }

    /**
     * @return {@code true}, если команда выполнена успешно
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @return текстовое сообщение ответа
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return дополнительные данные команды или {@code null}
     */
    public StringBuilder getData() {
        return data;
    }

    /**
     * @return {@code true}, если ответ предназначен только для внутренней обработки
     */
    public boolean isInternalOnly() {return internalOnly;}

    /**
     * @return записи коллекции или {@code null}
     */
    public List<MusicBandEntry> getEntries() {
        return entries;
    }

    /**
     * Задаёт список записей коллекции в ответе.
     *
     * @param entries элементы коллекции
     */
    public void setEntries(List<MusicBandEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "Response{\n" + "success=" + success + "\n, message=" + message + "\n, data=" + data + '}';
    }
}
