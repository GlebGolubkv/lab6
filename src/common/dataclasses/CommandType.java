package common.dataclasses;

import java.util.HashMap;
import java.util.Map;

/**
 * Типы команд клиента и сервера с метаданными: имя, описание и требования к аргументам.
 * Содержит проверку соответствия переданных аргумента и {@link MusicBand} ожиданиям команды.
 */
public enum CommandType {

    BEGIN_TRANSACTION(
            "begin_transaction",
            "начать транзакцию",
            false,
            false, false),
    CLEAR(
            "clear",
            "очистить коллекцию",
            false,
            false, false),
    COMMIT_TRANSACTION(
            "commit_transaction",
            "зафиксировать транзакцию",
            false,
            false, false),
    COUNT_BY_NUMBER_OF_PARTICIPANTS(
            "count_by_number_of_participants",
            "вывести количество элементов с заданным numberOfParticipants",
            true,
            false, false),
    EXECUTE_SCRIPT(
            "execute_script",
            "считать и исполнить скрипт из файла",
            true,
            false, false),

    FILTER_LESS_THEN_LABEL(
            "filter_less_then_label",
            "вывести элементы с label меньше заданного",
            true,
            false, false),
    HELP(
            "help",
            "вывести справку по командам",
            false,
            false, false),
    INFO(
            "info",
            "вывести информацию о коллекции",
            false,
            false, false),
    INSERT(
            "insert",
            "добавить новый элемент с заданным ключом",
            true,
            true, false),
    PRINT_FIELD_DESCENDING_LABEL(
            "print_field_descending_label",
            "вывести label в порядке убывания",
            false,
            false, false),
    REMOVE_KEY("remove_key",
            "удалить элемент по ключу",
            true,
            false, false),
    REMOVE_LOWER(
            "remove_lower",
            "удалить элементы меньшие чем заданный",
            false,
            true, false),
    REPLACE_IF_GREATER(
            "replace_if_greater",
            "заменить если новое больше",
            true,
            true, false),
    REPLACE_IF_LOWER(
            "replace_if_lower",
            "заменить если новое меньше",
            true,
            true, false),
    SHOW(
            "show",
            "вывести все элементы коллекции",
            false,
            false, false),
    UPDATE(
            "update",
            "обновить элемент по id",
            true,
            true, false),
    INSERT_USER("insert_user",
            "добавить пользователя",
            true,
            false, true),
    LOG_IN_USER("log_in_user",
            "войти в аккаунт",
            true,
            false, true),
    GET_COLLECTION(
            "get_collection",
            "получить коллекцию для GUI",
            false,
            false, true);

    private final String commandName;
    private final String description;
    private final boolean requiresArgument;
    private final boolean requiresMusicBand;
    private final boolean isInternalOnly;

    private static final Map<String, CommandType> BY_NAME = new HashMap<>();

    static {
        for (CommandType type : values()) {
            BY_NAME.put(type.commandName, type);
        }
    }

    CommandType(String commandName, String description, boolean requiresArgument, boolean requiresMusicBand, boolean isInternalOnly) {
        this.commandName = commandName;
        this.description = description;
        this.requiresArgument = requiresArgument;
        this.requiresMusicBand = requiresMusicBand;
        this.isInternalOnly = isInternalOnly;
    }

    /**
     * @return строковое имя команды для протокола
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * @return краткое описание команды
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return {@code true}, если команде нужен строковый аргумент
     */
    public boolean requiresArgument() {
        return requiresArgument;
    }

    /**
     * @return {@code true}, если команде нужен объект {@link MusicBand}
     */
    public boolean requiresMusicBand() {
        return requiresMusicBand;
    }

    /**
     * @return {@code true}, если команда только для внутреннего использования (авторизация, GUI)
     */
    public boolean isInternalOnly() {
        return isInternalOnly;
    }

    /**
     * Находит тип команды по строковому имени (без учёта регистра).
     *
     * @param name имя команды
     * @return соответствующий {@code CommandType} или {@code null}
     */
    public static CommandType fromName(String name) {
        if (name == null) return null;
        return BY_NAME.get(name.toLowerCase().trim());
    }

    /**
     * Проверяет, зарегистрировано ли имя команды.
     *
     * @param name имя команды
     * @return {@code true}, если команда известна
     */
    public static boolean contains(String name) {
        return name != null && BY_NAME.containsKey(name.toLowerCase().trim());
    }

    /**
     * Проверяет, что аргумент и объект группы соответствуют требованиям этой команды.
     *
     * @param argument  строковый аргумент команды
     * @param musicBand данные группы
     * @return {@code true}, если набор параметров допустим
     */
    public boolean validateInput(String argument, MusicBand musicBand) {
        boolean hasMusicBand = (musicBand != null);

        if (requiresArgument && argument == null) {
            return false;
        }
        if (requiresMusicBand && !hasMusicBand) {
            return false;
        }
        if (!requiresArgument && argument != null && !argument.isEmpty()) {
            return false;
        }
        if (!requiresMusicBand && hasMusicBand) {
            return false;
        }
        return true;
    }

    /**
     * Возвращает текст ошибки валидации или {@code null}, если параметры корректны.
     *
     * @param argument  строковый аргумент команды
     * @param musicBand данные группы
     * @return сообщение об ошибке либо {@code null}
     */
    public String getValidationError(String argument, MusicBand musicBand) {
        boolean hasMusicBand = (musicBand != null);

        if (requiresArgument && argument == null) {
            return "The command requires an argument";
        }
        if (requiresMusicBand && !hasMusicBand) {
            return "The command requires the input of MusicBand";
        }
        if (!requiresArgument && argument != null && !argument.isEmpty()) {
            return "The command does not accept arguments";
        }
        if (!requiresMusicBand && hasMusicBand) {
            return "The command does not accept the MusicBand";
        }
        return null;
    }

}
