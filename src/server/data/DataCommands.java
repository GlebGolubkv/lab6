package server.data;

import common.dataclasses.MusicBand;
import common.dataclasses.CommandType;
import common.Response;
import server.commands.*;
import server.commands.clientcommands.*;
import server.commands.internalcommands.GetCollection;
import server.commands.internalcommands.InsertUser;
import server.commands.internalcommands.LogInUser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Реестр серверных команд: инициализация, поиск и выполнение по типу {@link CommandType}.
 */
public class DataCommands {

    private static final HashMap<CommandType, Command> commands = new HashMap<>();
    private static DataCommands instance;

    /**
     * Возвращает единственный экземпляр реестра команд.
     *
     * @return инициализированный {@link DataCommands}
     * @throws IllegalStateException если реестр ещё не инициализирован
     */
    public static DataCommands getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DataCommands has not been initialized");
        }
        return instance;
    }

    /**
     * Инициализирует реестр команд (выполняется один раз при старте сервера).
     *
     * @throws IllegalStateException если реестр уже инициализирован
     */
    public static void initialize() {
        if (instance == null) {
            instance = new DataCommands();
        } else {
            throw new IllegalStateException("DataCommands has already been initialized");
        }
    }

    private DataCommands() {
        commands.put(CommandType.HELP, new Help());
        commands.put(CommandType.INFO, new Info());
        commands.put(CommandType.SHOW, new Show());
        commands.put(CommandType.INSERT, new Insert());
        commands.put(CommandType.UPDATE, new Update());
        commands.put(CommandType.REMOVE_KEY, new RemoveKey());
        commands.put(CommandType.CLEAR, new Clear());
        commands.put(CommandType.EXECUTE_SCRIPT, new ExecuteScript());
        commands.put(CommandType.REMOVE_LOWER, new RemoveLower());
        commands.put(CommandType.REPLACE_IF_GREATER, new ReplaceIfGreater());
        commands.put(CommandType.REPLACE_IF_LOWER, new ReplaceIfLower());
        commands.put(CommandType.COUNT_BY_NUMBER_OF_PARTICIPANTS, new CountByNumberOfParticipants());
        commands.put(CommandType.FILTER_LESS_THEN_LABEL, new FilterLessThanLabel());
        commands.put(CommandType.PRINT_FIELD_DESCENDING_LABEL, new PrintFieldDescendingLabel());
        commands.put(CommandType.INSERT_USER, new InsertUser());
        commands.put(CommandType.LOG_IN_USER, new LogInUser());
        commands.put(CommandType.GET_COLLECTION, new GetCollection());
    }

    /**
     * Возвращает список зарегистрированных типов команд.
     *
     * @return копия списка типов команд
     */
    public ArrayList<CommandType> getCommands() {
        return new ArrayList<>(commands.keySet());
    }

    /**
     * Добавляет или заменяет команду в реестре.
     *
     * @param commandName тип команды
     * @param command     экземпляр команды
     */
    public void addCommand(CommandType commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     * Возвращает экземпляр команды по типу.
     *
     * @param commandName тип команды
     * @return команда или {@code null}, если не найдена
     */
    public Command getCommand(CommandType commandName) {
        return commands.get(commandName);
    }

    /**
     * Возвращает строковое имя команды по типу.
     *
     * @param commandType тип команды
     * @return имя команды
     */
    public String getCommandName(CommandType commandType) {
        return  commandType.getCommandName();
    }

    /**
     * Создаёт и выполняет команду с учётом требуемых аргументов.
     *
     * @param commandType тип команды
     * @param argument    строковый аргумент (может быть {@code null})
     * @param musicBand   объект музыкальной группы (может быть {@code null})
     * @param clientId    идентификатор клиента
     * @return ответ сервера
     */
    public Response createCommand(CommandType commandType, String argument, MusicBand musicBand, int clientId) {

        if (commandType.requiresArgument() && commandType.requiresMusicBand()){

            return commands.get(commandType).execute(argument, musicBand, clientId);

        } else if (commandType.requiresArgument()){
            return commands.get(commandType).execute(argument, clientId);

        } else if (commandType.requiresMusicBand()){
            return commands.get(commandType).execute(musicBand, clientId);

        } else  {
            return commands.get(commandType).execute(clientId);
        }
    }

}
