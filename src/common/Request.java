package common;

import com.fasterxml.jackson.core.JsonProcessingException;
import common.dataclasses.CommandType;
import common.dataclasses.MusicBand;

/**
 * Запрос клиента к серверу: тип команды, аргумент, данные группы и идентификатор клиента.
 * Сериализуется в JSON для передачи по сети.
 */
public class Request {
    private CommandType commandType;
    private String argument;
    private MusicBand musicBand;
    private int clientId;

    /**
     * Конструктор по умолчанию для десериализации JSON.
     */
    public Request() {
    }

    /**
     * Задаёт тип выполняемой команды.
     *
     * @param commandType тип команды
     */
    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    /**
     * Создаёт запрос с указанными полями.
     *
     * @param commandType тип команды
     * @param argument    строковый аргумент команды
     * @param musicBand   данные группы (может быть {@code null})
     * @param clientId    идентификатор клиента
     */
    public Request(CommandType commandType, String argument, MusicBand musicBand, int clientId) {
        this.commandType = commandType;
        this.argument = argument;
        this.musicBand = musicBand;
        this.clientId = clientId;
    }

    /**
     * Преобразует запрос в JSON-строку.
     *
     * @return JSON-представление объекта
     * @throws JsonProcessingException при ошибке сериализации
     */
    public String toJson() throws JsonProcessingException {
        return JsonDataMapper.getInstance().getMapper().writeValueAsString(this);
    }

    /**
     * Восстанавливает запрос из JSON-строки.
     *
     * @param json JSON-представление запроса
     * @return десериализованный {@code Request}
     * @throws JsonProcessingException при ошибке разбора
     */
    public static Request fromJson(String json) throws JsonProcessingException {
        return JsonDataMapper.getInstance().getMapper().readValue(json, Request.class);

    }

    /**
     * @return тип команды
     */
    public CommandType getCommandType() {
        return commandType;
    }

    /**
     * @return данные музыкальной группы
     */
    public MusicBand getMusicBand() {
        return musicBand;
    }

    /**
     * @return строковый аргумент команды
     */
    public String getArgument() {
        return argument;
    }

    /**
     * @return идентификатор клиента
     */
    public int getClientId() {
        return clientId;
    }
}
