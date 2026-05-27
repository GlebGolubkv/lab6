package server.commands;

import common.dataclasses.MusicBand;
import common.Response;

/**
 * Абстрактный базовый класс серверной команды.
 * Определяет перегрузки {@link #execute} для разных наборов аргументов и описание команды.
 */
public abstract class Command {

    /**
     * Создаёт экземпляр команды.
     */
    public Command() {}

    /**
     * Выполняет команду без дополнительных аргументов.
     *
     * @param clintId идентификатор клиента
     * @return ответ сервера
     */
    public abstract Response execute(int clintId);

    /**
     * Выполняет команду со строковым аргументом.
     *
     * @param value1  строковый аргумент команды
     * @param clintId идентификатор клиента
     * @return ответ сервера
     */
    public abstract Response execute(String value1, int clintId);

    /**
     * Выполняет команду со строковым аргументом и объектом {@link MusicBand}.
     *
     * @param value1  строковый аргумент команды
     * @param value2  музыкальная группа
     * @param clintId идентификатор клиента
     * @return ответ сервера
     */
    public abstract Response execute(String value1, MusicBand value2, int clintId);

    /**
     * Выполняет команду с объектом {@link MusicBand}.
     *
     * @param value1  музыкальная группа
     * @param clintId идентификатор клиента
     * @return ответ сервера
     */
    public abstract Response execute(MusicBand value1, int clintId);

    /**
     * Возвращает краткое описание назначения команды.
     *
     * @return текст описания команды
     */
    public abstract String commandInfo();
}
